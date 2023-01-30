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
    ActivityResultLauncher<Intent> resultLauncher=Launcher.create(this,(listUri -> {
        raPageSet.addPageSets(listUri);
    }));
    Intent pdfPicker = Launcher.createPicker("application/pdf",true);

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

        raPageSet.addPageSets(Launcher.getUriData(getIntent()));

        loadingDialog=new LoadingDialog(this,"Merging...");
        nameDialog=new NameDialog(this);
        nameDialog.setOnConfirmListener(this::merge);

        binding.cardAdd.setOnClickListener(view -> resultLauncher.launch(pdfPicker));

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

}