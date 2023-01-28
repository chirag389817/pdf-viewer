package com.csp.pdfviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.csp.pdfviewer.adapters.RAPagesSelector;
import com.csp.pdfviewer.databinding.ActivitySelectPagesBinding;
import com.csp.pdfviewer.dialogs.LoadingDialog;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Thumbnails;

public class SelectPagesActivity extends AppCompatActivity {

    private static final String TAG = "SelectPagesActivity";
    public static final String LIST_KEY = "result_list_key";

    ActivitySelectPagesBinding binding;
    RAPagesSelector adapterSelectPages;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectPagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ACBar.setActionBar(this,binding.getRoot(),"Select Pages");

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();


        new Handler().post(()->{
            Thumbnails thumbnails = new Thumbnails(this,getIntent().getData());
            runOnUiThread(()->{
                binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3));
                adapterSelectPages = new RAPagesSelector(this,thumbnails,getIntent().getIntegerArrayListExtra(LIST_KEY));
                binding.recyclerView.setAdapter(adapterSelectPages);
                loadingDialog.dismiss();
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.add(301,302,303,"done")
                .setIcon(R.drawable.ic_baseline_check_24)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("done")){
            Intent goBackIntent=new Intent();
            goBackIntent.putIntegerArrayListExtra(LIST_KEY,adapterSelectPages.getSelectedPages());
            setResult(RESULT_OK,goBackIntent);
            finish();
        }
        return true;
    }
}