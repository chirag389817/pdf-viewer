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
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.util.ArrayList;

public class RecyclerAdapterSelectPages extends RecyclerView.Adapter<RecyclerAdapterSelectPages.ViewHolder> {

    Context context;
    PdfInfo pdfInfo;
    ArrayList<Integer> selectedPages = new ArrayList<>();

    private static final String TAG = "SelectPages";

    public RecyclerAdapterSelectPages(Context context, PdfInfo pdfInfo, ArrayList<Integer> selectedPages) {
        this.context = context;
        this.pdfInfo = pdfInfo;
        this.selectedPages=selectedPages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_page_thumbnail,parent,false));
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+position);
        holder.imgThumbnail.setImageBitmap(pdfInfo.getPageThumnail(position));
        if(selectedPages.contains(position)){
            holder.txtPageNumber.setText(Integer.toString(selectedPages.indexOf(position)+1));
            holder.txtPageNumber.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(view -> {
            if(holder.txtPageNumber.getVisibility()==View.VISIBLE){
                holder.txtPageNumber.setVisibility(View.GONE);
                selectedPages.remove(selectedPages.indexOf(position));
                notifyDataSetChanged();
            }else{
                holder.txtPageNumber.setVisibility(View.VISIBLE);
                selectedPages.add(position);
                holder.txtPageNumber.setText(Integer.toString(selectedPages.size()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfInfo.pageCount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtPageNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail=itemView.findViewById(R.id.imgThumbnail);
            txtPageNumber=itemView.findViewById(R.id.txtPageNumber);
        }
    }

    public ArrayList<Integer> getSelectedPages(){
        return selectedPages;
    }
}
