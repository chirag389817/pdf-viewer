package com.csp.pdfviewer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMyActionBar();
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode()== Activity.RESULT_OK){
            Intent openPdfIntent=new Intent(MainActivity.this,PdfViewer.class);
            openPdfIntent.setData(result.getData().getData());
            startActivity(openPdfIntent);
        }
    });

    public void openFile(View view) {
        Intent pdfPickerIntent =new Intent();
        pdfPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        pdfPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        pdfPickerIntent.setType("application/pdf");
        Intent picker=Intent.createChooser(pdfPickerIntent,"Choose PDF File");
        launcher.launch(picker);
    }
}