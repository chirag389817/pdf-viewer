package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.csp.pdfviewer.adapters.RAFileItem;
import com.csp.pdfviewer.databinding.ActivityResultBinding;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.FileInfo;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {


    public static final String LIST_KEY = "List_key";
    ArrayList<FileInfo> listFileInfo=new ArrayList<>();

    ActivityResultBinding binding;
    Intent pdfOpner=new Intent(this,PdfViewerActivity.class);
    Intent imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadResult();

        if(listFileInfo.get(0).name.endsWith(".pdf")){
            ACBar.setActionBar(this,binding.getRoot(),"Splited PDFs");
        }else{
            ACBar.setActionBar(this,binding.getRoot(),"Converted Images");
        }

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RAFileItem adapter = new RAFileItem(this, listFileInfo, ( position) -> {
            FileInfo fileInfo=listFileInfo.get(position);
            if(fileInfo.name.endsWith(".pdf")){
                pdfOpner.setData(Uri.fromFile(fileInfo.file));
                startActivity(pdfOpner);
            }else{
                imageViewer.putExtra(ImageViewerActivity.POSITION,position);
                startActivity(imageViewer);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

    private void loadResult() {
        for(String path:getIntent().getStringArrayListExtra(LIST_KEY))
            listFileInfo.add(new FileInfo(this,path));
        imageViewer  = new Intent(this,ImageViewerActivity.class);
        imageViewer.putExtra(ImageViewerActivity.TYPE,ImageViewerActivity.PATH_LIST);
        imageViewer.putExtra(ImageViewerActivity.LIST_KEY,getIntent().getStringArrayListExtra(LIST_KEY));
    }

}