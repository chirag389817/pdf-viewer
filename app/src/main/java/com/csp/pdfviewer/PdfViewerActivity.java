package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import com.csp.pdfviewer.utilclasses.PdfInfo;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfViewerActivity extends AppCompatActivity {

    PdfInfo pdfInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        setMyActionBar();

        pdfInfo=new PdfInfo(this,getIntent().getData());

        PDFView pdfView=findViewById(R.id.pdfView);
        getSupportActionBar().setTitle(pdfInfo.name);
        pdfView.fromUri(pdfInfo.uri)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}