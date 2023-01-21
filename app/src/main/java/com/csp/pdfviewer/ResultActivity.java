package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.csp.pdfviewer.adapters.RecyclerAdapterResult;

import java.io.File;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {


    public static final String RESULT_KEY = "Result_key";
    public static final String LIST_KEY = "List_key";

    private static final String TAG="ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setMyActionBar();
        loadResult();
    }

    private void loadResult() {
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG,"Setting Adater");
        recyclerView.setAdapter(new RecyclerAdapterResult(this,getIntent().getStringArrayListExtra(LIST_KEY)));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(RESULT_KEY));
    }
}