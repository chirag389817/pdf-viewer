package com.csp.pdfviewer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.csp.pdfviewer.adapters.GridViewAdapterMain;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int REQ_CODE_OPEN=101;
    final int REQ_CODE_MERGE=102;
    final int REQ_CODE_SPLIT=103;
    int req_code=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMyActionBar();
        setGridView();
    }

    private void setGridView() {
        GridView gridView=findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapterMain(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:openFile(REQ_CODE_OPEN);
                        break;
                    case 1:openFile(REQ_CODE_MERGE);
                        break;
                    case 2:openFile(REQ_CODE_SPLIT);
                        break;
                }
            }
        });
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openFile(int requestCode) {
        Intent pdfPickerIntent =new Intent();
        if(requestCode==REQ_CODE_MERGE){
            pdfPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        }else {
            pdfPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
        }
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
                case REQ_CODE_MERGE:mergePdfs(result.getData());
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

    private void mergePdfs(Intent intent) {
        ArrayList<Uri> fileUris=new ArrayList<>();
        if(intent.getData()!=null){
            fileUris.add(intent.getData());
        }
        if(intent.getClipData()!=null){
            for(int i=0; i<intent.getClipData().getItemCount(); i++){
                fileUris.add(intent.getClipData().getItemAt(i).getUri());
            }
        }
        Toast.makeText(this, Integer.toString(fileUris.size()), Toast.LENGTH_SHORT).show();
    }

    private void openPdf(Uri fileUri) {
        Intent openPdfIntent=new Intent(MainActivity.this, PdfViewerActivity.class);
        openPdfIntent.setData(fileUri);
        startActivity(openPdfIntent);
    }


}