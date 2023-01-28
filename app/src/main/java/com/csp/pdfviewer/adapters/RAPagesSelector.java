package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.R;
import com.csp.pdfviewer.utilclasses.Thumbnails;

import java.util.ArrayList;

public class RAPagesSelector extends RecyclerView.Adapter<RAPagesSelector.ViewHolder> {

    Context context;
    Thumbnails thumbnails;
    ArrayList<Integer> selectedPages = new ArrayList<>();

    private static final String TAG = "SelectPages";

    public RAPagesSelector(Context context, Thumbnails thumbnails, ArrayList<Integer> selectedPages) {
        this.context = context;
        this.thumbnails=thumbnails;
        this.selectedPages=selectedPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_page_thumbnail,parent,false),this);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        holder.imgThumbnail.setImageBitmap(thumbnails.get(position));

        if(selectedPages.contains(position)){
            holder.txtPageNumber.setText(Integer.toString(selectedPages.indexOf(position)+1));
            holder.txtPageNumber.setVisibility(View.VISIBLE);
        }else {
            holder.txtPageNumber.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return thumbnails.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtPageNumber;
        @SuppressLint("SetTextI18n")
        public ViewHolder(@NonNull View itemView, RAPagesSelector adapter) {
            super(itemView);
            imgThumbnail=itemView.findViewById(R.id.imgThumbnail);
            txtPageNumber=itemView.findViewById(R.id.txtPageNumber);

            itemView.setOnClickListener(view -> {
                Log.d(TAG, "ViewHolder getadapter: "+getAdapterPosition());
                if(txtPageNumber.getVisibility()==View.VISIBLE){
                    txtPageNumber.setVisibility(View.GONE);
                    adapter.selectedPages.remove((Integer) getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }else{
                    txtPageNumber.setVisibility(View.VISIBLE);
                    adapter.selectedPages.add(getAdapterPosition());
                    txtPageNumber.setText(Integer.toString(adapter.selectedPages.size()));
                }
            });

        }
    }

    public ArrayList<Integer> getSelectedPages(){
        return selectedPages;
    }
}
