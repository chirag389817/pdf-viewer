package com.csp.pdfviewer.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.R;
import com.csp.pdfviewer.databinding.ItemThumbnailBinding;
import com.csp.pdfviewer.utilclasses.Image;

import java.util.ArrayList;

public class RAImgToPdf extends RABase<RAImgToPdf.ViewHolder>{

    ArrayList<Image> imagesList=new ArrayList<>();
    Context context;

    public RAImgToPdf(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_thumbnail,parent,false);
        return new ViewHolder(ItemThumbnailBinding.bind(itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.imgThumbnail.setImageBitmap(imagesList.get(position).bitmap);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    @Override
    public ArrayList getList() {
        return imagesList;
    }

    @Override
    public void remove(int position) {
        imagesList.remove(position);
        notifyItemRemoved(position);
    }

    public void addImage(Uri uri){
        imagesList.add(new Image(context,uri));
        notifyItemInserted(imagesList.size()-1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemThumbnailBinding binding;
        public ViewHolder(@NonNull ItemThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.imgThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
