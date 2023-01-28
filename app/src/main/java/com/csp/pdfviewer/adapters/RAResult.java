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
import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.io.File;
import java.util.ArrayList;

public class RAResult extends RecyclerView.Adapter<RAResult.ViewHolder> {

    Context context;
    ArrayList<String> listPaths;

    private static final String TAG = "RAResult";

    public RAResult(Context context, ArrayList<String> listPaths) {
        this.context = context;
        this.listPaths = listPaths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: "+position);

        File file = new File(listPaths.get(position));
        String fileInfo = "";
        if(FileManager.getExtension(file).equals("pdf")){
            fileInfo=PdfInfo.getPageCount(context,Uri.fromFile(file))+" Pages ";
        }
        fileInfo = fileInfo+FileManager.getSize(file);

        holder.txtName.setText(file.getName());
        holder.txtInfo.setText(fileInfo);
        holder.imgThumbnail.setImageBitmap(FileManager.getThumbnail(context,file));

        holder.itemView.setOnClickListener(view -> {
            Intent openPdf = new Intent(context, PdfViewerActivity.class);
            openPdf.setData(Uri.fromFile(file));
            context.startActivity(openPdf);
        });

    }

    @Override
    public int getItemCount() {
        return listPaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtName,txtInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail=itemView.findViewById(R.id.imgThumbnail);
            txtName=itemView.findViewById(R.id.txtName);
            txtInfo=itemView.findViewById(R.id.txtInfo);
        }
    }
}
