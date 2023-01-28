package com.csp.pdfviewer.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class Drager extends ItemTouchHelper.Callback {

    RABase adapter;

    public Drager(RABase adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }



    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END;
        int swipe = ItemTouchHelper.END;
        return makeMovementFlags(drag,swipe);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition=viewHolder.getAdapterPosition();
        int toPosition=target.getAdapterPosition();
//        Collections.swap(adapter.getPageSetArrayList(),fromPosition,toPosition);
        Collections.swap(adapter.getList(),fromPosition,toPosition);
        adapter.notifyItemMoved(fromPosition,toPosition);
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position=viewHolder.getAdapterPosition();
//        adapter.removePageSet(position);
        adapter.remove(position);
    }

}
