package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.R;
import com.csp.pdfviewer.SelectPagesActivity;
import com.csp.pdfviewer.databinding.ItemPageSetBinding;
import com.csp.pdfviewer.utilclasses.PageSet;
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.util.ArrayList;

public class RAPageSet extends RABase<VHPageSet>{

    private static final String TAG = "RAPageSet";
    public static final String ACTION_MERGE = "Merge";
    public static final String ACTION_SPLIT = "Split";

    ArrayList<PageSet> pageSetArrayList=new ArrayList<>();
    int currentPosition;

    Context context;
    String  action;
    PdfInfo pdfToSplit;
    ActivityResultLauncher<Intent> launcher;
    int count=1;


    public RAPageSet(Context context) {
        this.context = context;
        this.action=ACTION_MERGE;
        setLauncher();
    }

    public RAPageSet(Context context, PdfInfo pdfToSplit){
        this.context=context;
        this.action=ACTION_SPLIT;
        this.pdfToSplit=pdfToSplit;
        setLauncher();
    }

    @NonNull
    @Override
    public VHPageSet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_page_set,parent,false);
        return new VHPageSet(this,ItemPageSetBinding.bind(view));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VHPageSet holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        holder.setPageSet(pageSetArrayList.get(position));
        holder.loadSelectedPages();
    }

    @Override
    public int getItemCount() {
        return pageSetArrayList.size();
    }

    public void addPageSets(ArrayList<Uri> listUri){
        for(Uri uri:listUri)
            pageSetArrayList.add(new PageSet(uri));
        notifyItemRangeInserted(this.pageSetArrayList.size()-listUri.size(),pageSetArrayList.size());
    }

    public void addPageSet(PageSet pageSet){
        pageSetArrayList.add(pageSet);
        notifyItemInserted(pageSetArrayList.size()-1);
    }

    public void addSplitSet(){
        addPageSet(new PageSet(0,0,"Split_"+(count++)));
    }

    public void resetSplitSets(PdfInfo pdfToSplit){
        this.pdfToSplit=pdfToSplit;
        pageSetArrayList.clear();
        count=1;
        notifyDataSetChanged();
        addSplitSet();
    }

    public ArrayList<PageSet> getPageSetArrayList() {
        return pageSetArrayList;
    }

    private void setLauncher(){
        launcher=((AppCompatActivity)context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK) {
                        pageSetArrayList.get(currentPosition).selectedPages = result.getData().getIntegerArrayListExtra(SelectPagesActivity.LIST_KEY);
                        notifyItemChanged(currentPosition);
                    }
                }
        );
    }

    @Override
    public ArrayList getList() {
        return pageSetArrayList;
    }

    @Override
    public void remove(int position) {
        pageSetArrayList.remove(position);
        notifyItemRemoved(position);
    }
}
