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

public class MainActivity extends AppCompatActivity {

    final int REQ_CODE_OPEN=101;
    final int REQ_CODE_SPLIT=103;

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
                    case 0:openFile(REQ_CODE_OPEN);
                        break;
                    case 1: {
                        Intent mergeIntent = new Intent(MainActivity.this, MergePdfActivity.class);
                        startActivity(mergeIntent);
                    }break;
                    case 2:openFile(REQ_CODE_SPLIT);
                        break;
                }
            }
        });
    }


    public void openFile(int requestCode) {
        Intent pdfPickerIntent =new Intent();
        pdfPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
        pdfPickerIntent.putExtra("reqCode",requestCode);
        pdfPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        pdfPickerIntent.setType("application/pdf");
        Intent picker=Intent.createChooser(pdfPickerIntent,"Choose PDF File");
        req_code=requestCode;
        launcher.launch(picker);
    }

    ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            switch (req_code){
                case REQ_CODE_OPEN:openPdf(result.getData().getData());
                    break;
                case REQ_CODE_SPLIT:splitPdf(result.getData().getData());
                    break;
            }
        }
        req_code=-1;
    });

    private void splitPdf(Uri fileUri) {
        Intent openPdfIntent=new Intent(MainActivity.this,SplitPdfActivity.class);
        openPdfIntent.setData(fileUri);
        startActivity(openPdfIntent);
    }

    private void openPdf(Uri fileUri) {
        Intent openPdfIntent=new Intent(MainActivity.this, PdfViewerActivity.class);
        openPdfIntent.setData(fileUri);
        startActivity(openPdfIntent);
    }

}