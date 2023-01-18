package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        setMyActionBar();

        PDFView pdfView=findViewById(R.id.pdfView);
        getSupportActionBar().setTitle(getFileName(getIntent().getData()));
        pdfView.fromUri(getIntent().getData())
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri){
        String name=null;
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        try{
            if(cursor!=null && cursor.moveToFirst())
                name=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }finally {
            cursor.close();
        }
        return  name;
    }
}