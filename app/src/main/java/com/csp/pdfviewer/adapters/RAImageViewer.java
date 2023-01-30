package com.csp.pdfviewer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.R;
import com.csp.pdfviewer.databinding.ItemImageBinding;
import com.csp.pdfviewer.utilclasses.Image;

import java.util.ArrayList;

public class RAImageViewer extends RecyclerView.Adapter<RAImageViewer.ViewHolder> {

    ArrayList<Image> listImages;

    public RAImageViewer(ArrayList<Image> listImages) {
        this.listImages = listImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        ItemImageBinding binding=ItemImageBinding.bind(itemView);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = listImages.get(position);
        holder.binding.imageView.setImageBitmap(image.bitmap);
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;
        public ViewHolder(@NonNull ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
