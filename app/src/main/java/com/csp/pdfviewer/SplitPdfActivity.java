package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.csp.pdfviewer.adapters.RecyclerAdapterPageset;
import com.csp.pdfviewer.tools.PdfSplitter;
import com.csp.pdfviewer.utilclasses.PdfInfo;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;

public class SplitPdfActivity extends AppCompatActivity {

    ImageView imgThumbnail;
    TextView txtPdfName, txtPdfInfo;
    CardView cardAddSet, cardSplit, cardView, cardReplace;
    PdfInfo pdfInfo;
    RecyclerAdapterPageset adapterPageset;
    RecyclerView recyclerView;

    private static final String TAG = "SplitPdfActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_pdf);

        initialize();
        PDFBoxResourceLoader.init(getApplicationContext());
        initialLogic();

        setMyActionBar();
        loadPdfDetails(getIntent().getData());

    }

    ActivityResultLauncher<Intent> launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            loadPdfDetails(result.getData().getData());
        }
    });

    ActivityResultLauncher<Intent> selectPagesLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        if(result.getResultCode()==RESULT_OK){
            adapterPageset.updatePageSet(result.getData());
        }
    });

    private void initialLogic() {
        cardSplit.setOnClickListener(view -> {
            View edit=this.getCurrentFocus();
            if(edit!=null){
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
                edit.clearFocus();
            }

            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Splitting...");
            progressDialog.setTitle(pdfInfo.name);
            progressDialog.show();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    PdfSplitter splitter = new PdfSplitter(getApplicationContext());
                    splitter.split(pdfInfo,adapterPageset.getPageSetArrayList());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(splitter.splitedPdfs.size()>0){
                                Intent showResult=new Intent(SplitPdfActivity.this,ResultActivity.class);
                                showResult.putExtra(ResultActivity.RESULT_KEY,"Splited PDFs");
                                showResult.putExtra(ResultActivity.LIST_KEY,splitter.splitedPdfs);
                                startActivity(showResult);
                                finish();
                            }
                        }
                    });
                }
            });
        });
        cardView.setOnClickListener(view->{
            Intent openPdfIntent=new Intent(this, PdfViewerActivity.class);
            openPdfIntent.setData(pdfInfo.uri);
            startActivity(openPdfIntent);
        });
        cardReplace.setOnClickListener(view -> {
            Intent pickerIntent=new Intent();
            pickerIntent.setAction(Intent.ACTION_GET_CONTENT);
            pickerIntent.setType("application/pdf");
            pickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
            launcher.launch(Intent.createChooser(pickerIntent,"Select PDF"));
        });
        cardAddSet.setOnClickListener(view -> {
            adapterPageset.addPageSet();
            txtPdfName.requestFocus();
        });
    }

    private void initialize() {
        imgThumbnail=findViewById(R.id.imgThumbnail);
        txtPdfName=findViewById(R.id.txtPdfName);
        txtPdfInfo=findViewById(R.id.txtPdfInfo);
        cardView=findViewById(R.id.cardView);
        cardReplace=findViewById(R.id.cardReplace);
        cardSplit=findViewById(R.id.cardSplit);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        cardAddSet=findViewById(R.id.cardAddSet);
    }

    private void loadPdfDetails(Uri uri) {
        pdfInfo=new PdfInfo(this,uri);
        txtPdfName.setText(pdfInfo.name);
        txtPdfInfo.setText(pdfInfo.pageCount+" Pages "+pdfInfo.size);
        imgThumbnail.setImageBitmap(pdfInfo.getPageThumnail(0));

        adapterPageset=new RecyclerAdapterPageset(this,RecyclerAdapterPageset.ACTION_SPLIT,pdfInfo,selectPagesLauncher);
        recyclerView.setAdapter(adapterPageset);
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Split PDF");
    }

}