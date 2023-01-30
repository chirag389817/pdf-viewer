package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.PdfViewerActivity;
import com.csp.pdfviewer.R;
import com.csp.pdfviewer.databinding.ItemFileBinding;
import com.csp.pdfviewer.utilclasses.FileInfo;
import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.io.File;
import java.util.ArrayList;

public class RAResult extends RecyclerView.Adapter<RAResult.ViewHolder> {

    Context context;
    ArrayList<FileInfo> listFileInfo=new ArrayList<>();
    Intent pdfOpner;

    private static final String TAG = "RAResult";

    public RAResult(Context context, ArrayList<String> listPaths) {
        this.context = context;
        this.pdfOpner= new Intent(context, PdfViewerActivity.class);
        for(String path:listPaths)
            listFileInfo.add(new FileInfo(context,path));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file,parent,false);
        return new ViewHolder(ItemFileBinding.bind(view),this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FileInfo fileInfo = listFileInfo.get(position);
        holder.binding.txtName.setText(fileInfo.name);
        holder.binding.txtInfo.setText(fileInfo.info);
        holder.binding.imgThumbnail.setImageBitmap(fileInfo.thumbnail);

    }

    @Override
    public int getItemCount() {
        return listFileInfo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemFileBinding binding;
        public ViewHolder(@NonNull ItemFileBinding binding,RAResult adaper) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(view -> {
                FileInfo fileInfo=adaper.listFileInfo.get(getAdapterPosition());
                if(fileInfo.name.endsWith(".pdf")){
                    adaper.pdfOpner.setData(Uri.fromFile(fileInfo.file));
                    adaper.context.startActivity(adaper.pdfOpner);
                }
            });
        }
    }
}
