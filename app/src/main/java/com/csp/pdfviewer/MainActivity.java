package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.csp.pdfviewer.adapters.GAMain;
import com.csp.pdfviewer.databinding.ActivityMainBinding;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Launcher;

public class MainActivity extends AppCompatActivity {

    final int REQ_CODE_OPEN=101;
    final int REQ_CODE_MERGE=102;
    final int REQ_CODE_SPLIT=103;
    final int REQ_CODE_IMAGES_TO_PDF=104;

    Intent singlePdfPicker = Launcher.createPicker("application/pdf",false);
    Intent multiPdfPicker = Launcher.createPicker("application/pdf",true);
    Intent imagePicker = Launcher.createPicker("image/*",true);

    ActivityMainBinding binding;
    int req_code=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ACBar.setActionBar(this,binding.getRoot());
        setGridView();
    }

    private void setGridView() {
        binding.gridView.setAdapter(new GAMain(this));
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        req_code=REQ_CODE_OPEN;
                        launcher.launch(singlePdfPicker);
                        break;
                    case 1:
                        req_code=REQ_CODE_MERGE;
                        launcher.launch(multiPdfPicker);
                        break;
                    case 2:
                        req_code=REQ_CODE_SPLIT;
                        launcher.launch(singlePdfPicker);
                        break;
                    case 3:
                        req_code=REQ_CODE_IMAGES_TO_PDF;
                        launcher.launch(imagePicker);
                        break;
                }
            }
        });
    }

    ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()== Activity.RESULT_OK && result.getData()!=null){
            Intent intent=result.getData();
            switch (req_code){
                case REQ_CODE_OPEN:
                    intent.setClass(this,PdfViewerActivity.class);
                    break;
                case REQ_CODE_MERGE:
                    intent.setClass(this,MergePdfActivity.class);
                    break;
                case REQ_CODE_SPLIT:
                    intent.setClass(this,SplitPdfActivity.class);
                    break;
                case REQ_CODE_IMAGES_TO_PDF:
                    intent.setClass(this,ImageToPdfActivity.class);
                    break;
            }
            startActivity(intent);
        }
        req_code=-1;
    });

}