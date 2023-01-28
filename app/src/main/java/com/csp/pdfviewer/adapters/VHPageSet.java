package com.csp.pdfviewer.adapters;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.SelectPagesActivity;
import com.csp.pdfviewer.databinding.ItemPageSetBinding;
import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PageSet;

import java.util.ArrayList;
import java.util.Arrays;

public class VHPageSet extends RecyclerView.ViewHolder {

    private static final String TAG = "VHPageSet";

    ItemPageSetBinding binding;
    RAPageSet adapter;
    PageSet pageSet;

    public VHPageSet(RAPageSet adapter, @NonNull ItemPageSetBinding  binding) {
        super(binding.getRoot());
        this.binding=binding;
        this.adapter=adapter;
        setSpinners();
        enableChangesUpdate();
        setOnclickListeners();
    }

    private void setOnclickListeners() {
        binding.imgRemove.setOnClickListener(view->{
            Log.d(TAG, "setOnclickListeners: "+getAdapterPosition());
            adapter.remove(getAdapterPosition());
        });
        binding.cardSelectPages.setOnClickListener(view -> {
            adapter.currentPosition=getAdapterPosition();
            Intent selectPages = new Intent(adapter.context, SelectPagesActivity.class);
            if(adapter.action==RAPageSet.ACTION_MERGE){
                selectPages.setData(pageSet.uri);
            }else{
                selectPages.setData(adapter.pdfToSplit.uri);
            }
            selectPages.putExtra(SelectPagesActivity.LIST_KEY,pageSet.getSelectedPages());
            adapter.launcher.launch(selectPages);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setPageSet(PageSet pageSet){
            this.pageSet=pageSet;
            setAction();
            selectPageSetType();
            binding.spinPSType.setSelection(pageSet.typeCode-PageSet.TYPE_ALL);
    }

    private void setAction() {
        if(adapter.action.equals(RAPageSet.ACTION_MERGE)){
            binding.txtSave.setVisibility(View.GONE);
            loadPdfName();
        }else{
            loadName();
        }
        loadPageNumbers();
        loadSelectedPages();
    }

    private void selectPageSetType() {
        if (pageSet.typeCode==PageSet.TYPE_RANGE){
            binding.linRange.setVisibility(View.VISIBLE);
            binding.linCustom.setVisibility(View.GONE);
        }else if(pageSet.typeCode==PageSet.TYPE_CUSTOM){
            binding.linCustom.setVisibility(View.VISIBLE);
            binding.linRange.setVisibility(View.GONE);
        }else{
            binding.linRange.setVisibility(View.GONE);
            binding.linCustom.setVisibility(View.GONE);
        }
    }

    void setSpinners(){
        binding.spinPSType.setAdapter(new ArrayAdapter<String>(adapter.context, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList("All","Range","Custom"))));
        binding.spinPSType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                pageSet.typeCode=PageSet.TYPE_ALL+position;
                selectPageSetType();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    void enableChangesUpdate(){

        binding.editFrom.setOnFocusChangeListener((view, b) -> {
            if(!b){
                pageSet.fromPage= parseInt(binding.editFrom.getText().toString());
            }
        });
        binding.editTo.setOnFocusChangeListener((view, b) -> {
            if(!b){
                pageSet.toPage= parseInt(binding.editTo.getText().toString());
            }
        });
//        binding.editFrom.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void afterTextChanged(Editable editable) {
//                pageSet.fromPage=parseInt(binding.editFrom.getText().toString());
//            }
//        });
//        binding.editTo.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//            @Override
//            public void afterTextChanged(Editable editable) {
//                pageSet.toPage=parseInt(binding.editTo.getText().toString());
//            }
//        });

        binding.editName.setOnFocusChangeListener((view, b) -> {
            if(!b){
                pageSet.pdfName=binding.editName.getText().toString();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void loadPageNumbers(){
        binding.editFrom.setText(Integer.toString(pageSet.fromPage));
        binding.editTo.setText(Integer.toString(pageSet.toPage));
    }
    void loadName(){
        binding.editName.setText(pageSet.pdfName);
    }
    void loadPdfName(){
        binding.editName.setEnabled(false);
        binding.editName.setBackground(null);
        binding.editName.setText(FileManager.getName(adapter.context, pageSet.uri));
    }
    void loadSelectedPages(){
        binding.txtCustomPages.setText(pageSet.getSelectedPagesString());
    }
}
