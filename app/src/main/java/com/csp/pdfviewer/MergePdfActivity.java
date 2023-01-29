package com.csp.pdfviewer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csp.pdfviewer.adapters.Drager;
import com.csp.pdfviewer.adapters.RAPageSet;
import com.csp.pdfviewer.databinding.ActivityMergePdfBinding;
import com.csp.pdfviewer.databinding.DialogNameBinding;
import com.csp.pdfviewer.dialogs.LoadingDialog;
import com.csp.pdfviewer.dialogs.NameDialog;
import com.csp.pdfviewer.tools.PdfMerger;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Launcher;
import com.csp.pdfviewer.utilclasses.PageSet;

import java.io.File;
import java.util.ArrayList;

public class MergePdfActivity extends AppCompatActivity {

    private static final String TAG = "MergePdfActivity";

    ActivityMergePdfBinding binding;
    RAPageSet raPageSet;
    LoadingDialog loadingDialog;
    NameDialog nameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMergePdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ACBar.setActionBar(this,binding.getRoot(),"Merge PDFs");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        raPageSet=new RAPageSet(this);
        binding.recyclerView.setAdapter(raPageSet);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Drager(raPageSet));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

        addPdfs();

        loadingDialog=new LoadingDialog(this,"Merging...");
        nameDialog=new NameDialog(this);
        nameDialog.setOnConfirmListener(this::merge);

        binding.cardAdd.setOnClickListener(view -> addPdfs());

        binding.cardMerge.setOnClickListener(view -> {
            if(getCurrentFocus()!=null)
                getCurrentFocus().clearFocus();
            if(raPageSet.getItemCount()>1){
                nameDialog.show();
            }else{
                Toast.makeText(this, "Select at least two PDFs", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void merge(String fileName){
        loadingDialog.show();
        new Handler().post(() -> {
            File mergedPdf =new PdfMerger(MergePdfActivity.this).merge(raPageSet.getPageSetArrayList(),fileName);
            runOnUiThread(()->{
                loadingDialog.dismiss();
                if(mergedPdf!=null){
                    if(mergedPdf.exists()){
                        Intent viewPdf = new Intent(getApplicationContext(),PdfViewerActivity.class);
                        viewPdf.setData(Uri.fromFile(mergedPdf));
                        startActivity(viewPdf);
                        finish();
                    }
                }else {
                    Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    void addPdfs(){
        Intent selectPdfs=new Intent(Intent.ACTION_GET_CONTENT);
        selectPdfs.setType("application/pdf");
        selectPdfs.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        Intent picker=Intent.createChooser(selectPdfs,"Select PDFs");
        launcher.launch(picker);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK) {
                    ArrayList<PageSet> fileUris = new ArrayList<>();
                    if (result.getData().getData() != null) {
                        fileUris.add(new PageSet(result.getData().getData()));
                    }
                    if (result.getData().getClipData() != null) {
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            fileUris.add(new PageSet(result.getData().getClipData().getItemAt(i).getUri()));
                        }
                    }
                    raPageSet.addPageSets(fileUris);
                }

            }
    );

}