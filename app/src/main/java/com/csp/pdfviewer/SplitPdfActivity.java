package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.csp.pdfviewer.utilclasses.PdfInfo;

public class SplitPdfActivity extends AppCompatActivity {

    private static final String TAG = "SplitPdfActivity";
    ActivitySplitPdfBinding binding;

    PdfInfo pdfInfo;
    LoadingDialog loadingDialog;
    RAPageSet raPageSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplitPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        loadingDialog=new LoadingDialog(this);

        initialLogic();

        ACBar.setActionBar(this,binding.getRoot(),"Split PDF");

        loadPdfDetails(getIntent().getData());

        raPageSet=new RAPageSet(this,pdfInfo);
        binding.recyclerView.setAdapter(raPageSet);
        raPageSet.addSplitSet();

    }

    ActivityResultLauncher<Intent> launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            loadPdfDetails(result.getData().getData());
            raPageSet.resetSplitSets(pdfInfo);
        }
    });

    private void initialLogic() {
        binding.cardSplit.setOnClickListener(view -> {
            if(getCurrentFocus()!=null)
                getCurrentFocus().clearFocus();

            loadingDialog.show("Splitting...");

            new Handler().post(() -> {
                PdfSplitter splitter = new PdfSplitter(getApplicationContext());
                splitter.split(pdfInfo,raPageSet.getPageSetArrayList());
                runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    if(splitter.splitedPdfs.size()>0){
                        Intent showResult=new Intent(SplitPdfActivity.this,ResultActivity.class);
                        showResult.putExtra(ResultActivity.RESULT_TITLE_KEY,"Splited PDFs");
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
        binding.cardReplace.setOnClickListener(view -> {
            Intent pickerIntent=new Intent();
            pickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            pickerIntent.setType("application/pdf");
            pickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
            launcher.launch(Intent.createChooser(pickerIntent,"Select PDF"));
        });
        binding.cardAddSet.setOnClickListener(view -> {
            raPageSet.addSplitSet();
        });
    }


    private void loadPdfDetails(Uri uri) {
        pdfInfo=new PdfInfo(this,uri);
        binding.txtPdfName.setText(pdfInfo.name);
        binding.txtPdfInfo.setText(pdfInfo.pageCount+" Pages "+pdfInfo.size);
        binding.imgThumbnail.setImageBitmap(pdfInfo.getPageThumnail(0));
    }

}