package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.csp.pdfviewer.adapters.Drager;
import com.csp.pdfviewer.adapters.RAImgToPdf;
import com.csp.pdfviewer.databinding.ActivityImageToPdfBinding;
import com.csp.pdfviewer.dialogs.LoadingDialog;
import com.csp.pdfviewer.dialogs.NameDialog;
import com.csp.pdfviewer.tools.ImagesToPdf;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Launcher;

import java.io.File;
import java.util.ArrayList;

public class ImageToPdfActivity extends AppCompatActivity {

    ActivityImageToPdfBinding binding;
    RAImgToPdf adapter;
    LoadingDialog loadingDialog;
    NameDialog nameDialog;
    ActivityResultLauncher<Intent> resultLauncher=Launcher.create(this,this::loadImages);
    Intent imagePicker = Launcher.createPicker("image/*",true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageToPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ACBar.setActionBar(this,binding.getRoot(),"Image to PDF");

        loadingDialog=new LoadingDialog(this);
        nameDialog=new NameDialog(this,"Images to PDF");
        nameDialog.setOnConfirmListener(this::imagesToPdf);

        setUpRecyclerView();
        loadImages(Launcher.getUriData(getIntent()));

        binding.cardAdd.setOnClickListener(view ->resultLauncher.launch(imagePicker));
        binding.cardCreate.setOnClickListener(view -> {
            if(adapter.getItemCount()>0)
                nameDialog.show();
            else
                Toast.makeText(this, "Add Images", Toast.LENGTH_SHORT).show();
        });

    }

    private void loadImages(ArrayList<Uri> listUri) {
        for(Uri uri:listUri)
            adapter.addImage(uri);
    }

    private void setUpRecyclerView() {
        adapter=new RAImgToPdf(this);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new Drager(adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        binding.recyclerView.setAdapter(adapter);
    }

    private void imagesToPdf(String name){
        loadingDialog.show();
        new Handler().post(() -> {
            File file =new ImagesToPdf(this).create(adapter.getList(),name);
            runOnUiThread(()->{
                loadingDialog.dismiss();
                if(file!=null){
                    Intent openPdf=new Intent(this,PdfViewerActivity.class);
                    openPdf.setData(Uri.fromFile(file));
                    startActivity(openPdf);
                    finish();
                }
            });
        });
    }

}