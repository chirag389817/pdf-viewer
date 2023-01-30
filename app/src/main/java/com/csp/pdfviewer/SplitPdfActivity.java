package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.csp.pdfviewer.adapters.RAPageSet;
import com.csp.pdfviewer.databinding.ActivitySplitPdfBinding;
import com.csp.pdfviewer.dialogs.LoadingDialog;
import com.csp.pdfviewer.tools.PdfSplitter;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Launcher;
import com.csp.pdfviewer.utilclasses.PdfInfo;

public class SplitPdfActivity extends AppCompatActivity {

    private static final String TAG = "SplitPdfActivity";
    ActivitySplitPdfBinding binding;

    PdfInfo pdfInfo;
    LoadingDialog loadingDialog;
    RAPageSet adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplitPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ACBar.setActionBar(this,binding.getRoot(),"Split PDF");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        loadingDialog=new LoadingDialog(this,"Splitting...");

        initialLogic();
        loadPdfDetails();

        adapter =new RAPageSet(this,pdfInfo);
        binding.recyclerView.setAdapter(adapter);
        adapter.addSplitSet();

    }

    private void initialLogic() {

        binding.cardSplit.setOnClickListener(view -> {
            if(getCurrentFocus()!=null)
                getCurrentFocus().clearFocus();

            loadingDialog.show();

            new Handler().post(() -> {
                PdfSplitter splitter = new PdfSplitter(getApplicationContext());
                splitter.split(pdfInfo, adapter.getPageSetArrayList());
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    if(splitter.splitedPdfs.size()>0){
                        Intent showResult=new Intent(SplitPdfActivity.this,ResultActivity.class);
                        showResult.putExtra(ResultActivity.LIST_KEY,splitter.splitedPdfs);
                        startActivity(showResult);
                        finish();
                    }
                });
            });
        });

        binding.cardView.setOnClickListener(view->{
            Intent openPdfIntent=new Intent(this, PdfViewerActivity.class);
            openPdfIntent.setData(pdfInfo.uri);
            startActivity(openPdfIntent);
        });

        binding.cardAddSet.setOnClickListener(view -> {
            adapter.addSplitSet();
        });

    }


    @SuppressLint("SetTextI18n")
    private void loadPdfDetails() {
        pdfInfo=new PdfInfo(this, Launcher.getUriData(getIntent()).get(0));
        binding.txtPdfName.setText(pdfInfo.name);
        binding.txtPdfInfo.setText(pdfInfo.pageCount+" Pages "+pdfInfo.size);
        binding.imgThumbnail.setImageBitmap(pdfInfo.getPageThumnail(0));
    }

}