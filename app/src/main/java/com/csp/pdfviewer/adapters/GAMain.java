package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.csp.pdfviewer.R;

public class GAMain extends BaseAdapter {

    Context context;
    String[] itemNames={"Open","Merge","Split","Image to\nPDF","PDF to\nImages"};
    int[] itemIcons={R.drawable.open_pdf,R.drawable.merge_pdf,R.drawable.split_pdf,
            R.drawable.images_to_pdf,R.drawable.pdf_to_images};

    public GAMain(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_grid_main,viewGroup,false);
        ImageView btnicon=itemView.findViewById(R.id.btnicon);
        TextView txtItemName=itemView.findViewById(R.id.txtItemName);
        btnicon.setImageDrawable(context.getResources().getDrawable(itemIcons[position]));
        txtItemName.setText(itemNames[position]);
        return itemView;
    }
}
