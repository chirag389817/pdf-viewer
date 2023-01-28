package com.csp.pdfviewer.utilclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.csp.pdfviewer.R;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

public class ACBar {

    @SuppressLint("InflateParams")
    public static void setActionBar(Context context, ViewGroup parent){
        AppBarLayout appBarLayout = (AppBarLayout) LayoutInflater.from(context).inflate(R.layout.tool_bar,null,false);
        Toolbar toolbar = appBarLayout.findViewById(R.id.toolbar);
        parent.addView(appBarLayout,0,new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
    }

    @SuppressLint("InflateParams")
    public static void setActionBar(Context context, ViewGroup parent, String title){
        setActionBar(context,parent);
        Objects.requireNonNull(((AppCompatActivity) context).getSupportActionBar()).setTitle(title);
    }
}
