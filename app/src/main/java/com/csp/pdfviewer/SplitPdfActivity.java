package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SplitPdfActivity extends AppCompatActivity {

    ImageView imgThumbnail;
    TextView txtPdfName, txtPdfInfo;
    CardView cardAddSet, cardSplit, cardView, cardReplace;
    PdfInfo pdfInfo;
    RecyclerAdapterPageset adapterPageset;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_pdf);

        initialize();
        initialLogic();

        setMyActionBar();
        loadPdfDetails(getIntent().getData());

    }

    ActivityResultLauncher<Intent> launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            loadPdfDetails(result.getData().getData());
        }
    });

    private void initialLogic() {
        cardSplit.setOnClickListener(view -> {
            txtPdfName.requestFocus();
            View edit=this.getCurrentFocus();
            if(edit!=null){
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
                edit.clearFocus();
            }

            ArrayList<PageSet> pageSetArrayList=adapterPageset.getPageSetArrayList();
            String txt="";
            for(PageSet set:pageSetArrayList){
                txt=txt+set.fromPage+" ";
            }
            txtPdfName.setText(txt);

        });
        cardView.setOnClickListener(view->{
            Intent openPdfIntent=new Intent(this,PdfViewer.class);
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
        cardAddSet=findViewById(R.id.cardAddSet);
    }

    private void loadPdfDetails(Uri uri) {
        pdfInfo=new PdfInfo(this,uri);
        txtPdfName.setText(pdfInfo.name);
        txtPdfInfo.setText(pdfInfo.pageCount+" Pages "+pdfInfo.size);
        imgThumbnail.setImageBitmap(pdfInfo.getThumnail(0));

        adapterPageset=new RecyclerAdapterPageset(this,RecyclerAdapterPageset.ACTION_SPLIT,pdfInfo.name);
        recyclerView.setAdapter(adapterPageset);
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Split PDF");
    }

}