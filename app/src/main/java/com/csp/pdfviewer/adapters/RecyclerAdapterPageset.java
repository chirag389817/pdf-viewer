package com.csp.pdfviewer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.pdfviewer.R;
import com.csp.pdfviewer.utilclasses.PageSet;

import java.util.ArrayList;

public class RecyclerAdapterPageset extends RecyclerView.Adapter<RecyclerAdapterPageset.ViewHolder> {

    public static String ACTION_SPLIT="Split";
    public static String ACTION_MERGE="Merge";

    Context context;
    String action;
    String pdfName;
    int count=1;
    ArrayList<PageSet> pageSetArrayList =new ArrayList<>();

    public RecyclerAdapterPageset(Context context, String action,String pdfNmae){
        this.context=context;
        this.action=action;
        this.pdfName=pdfNmae;
        addPageSet();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addPageSet() {
        if(action.equals(ACTION_SPLIT)){
            pageSetArrayList.add(new PageSet(0,0,pdfName+"_split_"+ count++));
        }else{
            pageSetArrayList.add(new PageSet(pdfName));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_page_set,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.editName.setText(pageSetArrayList.get(position).getPdfName());
        holder.txtAction.setText(action + " Set " + (position + 1));
        holder.editFromPage.setText(Integer.toString(pageSetArrayList.get(position).getFromPage()));
        holder.editToPage.setText(Integer.toString(pageSetArrayList.get(position).getToPage()));
        setSelectType(holder,pageSetArrayList.get(position));
        holder.spinner.setSelection(pageSetArrayList.get(position).getTypeCode()-201);
        enableChangesToSave(position,holder);
    }



    private void enableChangesToSave(int position, ViewHolder holder) {
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int spinnerPosition, long l) {
                if(spinnerPosition==0) {
                    pageSetArrayList.get(position).setTypeCode(PageSet.TYPE_ALL);
                }else if(spinnerPosition==1){
                    pageSetArrayList.get(position).setTypeCode(PageSet.TYPE_RANGE);
                }else if(spinnerPosition==2){
                    pageSetArrayList.get(position).setTypeCode(PageSet.TYPE_CUSTOM);
                }
                setSelectType(holder, pageSetArrayList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        holder.imgRemove.setOnClickListener(view -> {
            pageSetArrayList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Removed...", Toast.LENGTH_SHORT).show();
        });
        holder.editFromPage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    int fromPage=Integer.parseInt(holder.editFromPage.getText().toString());
                    pageSetArrayList.get(position).setFromPage(fromPage);
                }
            }
        });
        holder.editToPage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    int toPage=Integer.parseInt(holder.editToPage.getText().toString());
                    pageSetArrayList.get(position).setToPage(toPage);
                }
            }
        });
        holder.editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    pageSetArrayList.get(position).setPdfName(holder.editName.getText().toString());
                }
            }
        });
    }

    private void setSelectType(ViewHolder holder,PageSet pageSet) {
        holder.linRange.setVisibility(View.GONE);
        holder.linCustom.setVisibility(View.GONE);
        if(pageSet.getTypeCode()==PageSet.TYPE_RANGE){
            holder.linRange.setVisibility(View.VISIBLE);
        }else if(pageSet.getTypeCode()==PageSet.TYPE_CUSTOM){
            holder.linCustom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pageSetArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAction;
        Spinner spinner;
        LinearLayout linRange,linCustom,linName;
        EditText editFromPage,editToPage,editName;
        ImageView imgRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAction=itemView.findViewById(R.id.txtAction);
            spinner=itemView.findViewById(R.id.spinner);
            linRange=itemView.findViewById(R.id.linRange);
            linCustom=itemView.findViewById(R.id.linCustom);
            linName=itemView.findViewById(R.id.linName);
            editFromPage=itemView.findViewById(R.id.editFrom);
            editToPage=itemView.findViewById(R.id.editTo);
            editName=itemView.findViewById(R.id.editName);
            imgRemove=itemView.findViewById(R.id.imgRemove);

            ArrayList<String> spinnerItems=new ArrayList<>();
            spinnerItems.add("All");
            spinnerItems.add("Range");
//            spinnerItems.add("Custom");
            ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,spinnerItems);
            spinner.setAdapter(spinnerAdapter);
        }
    }

    public ArrayList<PageSet> getPageSetArrayList(){
        return pageSetArrayList;
    }

}
