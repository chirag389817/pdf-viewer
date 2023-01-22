package com.csp.pdfviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.csp.pdfviewer.adapters.RecyclerAdapterSelectPages;
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.util.ArrayList;

public class SelectPagesActivity extends AppCompatActivity {

    RecyclerAdapterSelectPages adapterSelectPages;

    private static final String TAG = "SelectPagesActivity";
    public static final String LIST_KEY = "result_list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pages);

        setMyActionBar();

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapterSelectPages = new RecyclerAdapterSelectPages(this,new PdfInfo(this,getIntent().getData()),getIntent().getIntegerArrayListExtra(LIST_KEY));
        recyclerView.setAdapter(adapterSelectPages);
    }

    private void setMyActionBar() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Pages");
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

            ArrayList<Integer> selectedPages = adapterSelectPages.getSelectedPages();
            Log.d(TAG, "onOptionsItemSelected: "+selectedPages.size());

            Intent goBackIntent=new Intent(this,SplitPdfActivity.class);
            goBackIntent.putIntegerArrayListExtra(LIST_KEY,selectedPages);
            goBackIntent.putExtra("position",getIntent().getIntExtra("position",-1));
            setResult(RESULT_OK,goBackIntent);
            finish();

        }
        return true;
    }
}