package com.csp.pdfviewer.adapters;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class RABase<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {

    public abstract ArrayList getList();
    public abstract void remove(int position);
}
