package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import com.csp.pdfviewer.databinding.ActivityPdfViewerBinding;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.PdfInfo;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {

    ActivityPdfViewerBinding binding;
    PdfInfo pdfInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ACBar.setActionBar(this,binding.getRoot());

        pdfInfo=new PdfInfo(this,getIntent().getData());

        Objects.requireNonNull(getSupportActionBar()).setTitle(pdfInfo.name);
        binding.pdfView.fromUri(pdfInfo.uri)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

}