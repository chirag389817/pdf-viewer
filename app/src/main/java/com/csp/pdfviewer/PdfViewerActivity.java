package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.csp.pdfviewer.databinding.ActivityPdfViewerBinding;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.PdfInfo;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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
                .defaultPage(0)
                .spacing(10)
                .scrollHandle(new DefaultScrollHandle(this))
                .onRender((nbPages, pageWidth, pageHeight) -> {
                    binding.pdfView.fitToWidth();
                })
                .load();
    }

}