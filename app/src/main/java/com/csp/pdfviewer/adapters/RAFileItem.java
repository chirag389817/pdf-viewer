package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.PdfViewerActivity;
import com.csp.pdfviewer.R;
import com.csp.pdfviewer.databinding.ItemFileBinding;
import com.csp.pdfviewer.utilclasses.FileInfo;

import java.util.ArrayList;

public class RAFileItem extends RecyclerView.Adapter<RAFileItem.ViewHolder> {

    Context context;
    ArrayList<FileInfo> listFileInfo;
    ViewHolder.OnClickListener onClickListener;

    private static final String TAG = "RAFileItem";

    public RAFileItem(Context context, ArrayList<FileInfo> listFileInfo, ViewHolder.OnClickListener onClickListener) {
        this.context = context;
        this.listFileInfo=listFileInfo;
        this.onClickListener=onClickListener;
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

        public ViewHolder(@NonNull ItemFileBinding binding, RAFileItem adaper) {
            super(binding.getRoot());
            this.binding=binding;
            binding.getRoot().setOnClickListener(view -> {
                adaper.onClickListener.onClick(getAdapterPosition());
            });
        }

        public interface OnClickListener{
            void onClick(int position);
        }
    }
}
