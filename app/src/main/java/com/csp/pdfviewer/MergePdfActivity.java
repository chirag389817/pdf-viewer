package com.csp.pdfviewer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.csp.pdfviewer.adapters.Drager;
import com.csp.pdfviewer.adapters.RAPageSet;
import com.csp.pdfviewer.databinding.ActivityMergePdfBinding;
import com.csp.pdfviewer.databinding.DialogMergeBinding;
import com.csp.pdfviewer.tools.PdfMerger;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.PageSet;

import java.io.File;
import java.util.ArrayList;

public class MergePdfActivity extends AppCompatActivity {

    private static final String TAG = "MergePdfActivity";

    ActivityMergePdfBinding binding;
    RAPageSet raPageSet;
    ProgressDialog progressDialog;
    Dialog MergeDialog;

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



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Merging...");
        progressDialog.setCancelable(false);

        MergeDialog =new Dialog(this);
        DialogMergeBinding dialogBinding = DialogMergeBinding.inflate(getLayoutInflater());
        MergeDialog.setContentView(dialogBinding.getRoot());
        MergeDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        MergeDialog.setOnShowListener(dialogInterface -> {
            dialogBinding.editName.requestFocus();
        });
        dialogBinding.txtCancel.setOnClickListener(view -> MergeDialog.dismiss());
        dialogBinding.txtConfirm.setOnClickListener(view -> {
            MergeDialog.dismiss();
            progressDialog.show();
            merge(dialogBinding.editName.getText().toString());
        });

        binding.cardAdd.setOnClickListener(view-> addPdfs());

        binding.cardMerge.setOnClickListener(view -> {
            if(getCurrentFocus()!=null)
                getCurrentFocus().clearFocus();
            if(raPageSet.getItemCount()>1){
                MergeDialog.show();
            }else{
                Toast.makeText(this, "Select at least two PDFs", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void merge(String fileName){
        new Handler().post(() -> {
            File mergedPdf =new PdfMerger(MergePdfActivity.this).merge(raPageSet.getPageSetArrayList(),fileName);
            runOnUiThread(()->{
                progressDialog.dismiss();
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