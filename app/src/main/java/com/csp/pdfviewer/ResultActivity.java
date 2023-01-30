package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.csp.pdfviewer.adapters.RAResult;
import com.csp.pdfviewer.databinding.ActivityResultBinding;
import com.csp.pdfviewer.utilclasses.ACBar;

public class ResultActivity extends AppCompatActivity {


    public static final String LIST_KEY = "List_key";

    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadResult();
    }

    private void loadResult() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new RAResult(this,getIntent().getStringArrayListExtra(LIST_KEY)));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

}