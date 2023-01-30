package com.csp.pdfviewer.utilclasses;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class PageSet {

    public static final int TYPE_ALL = 201;
    public static final int TYPE_RANGE = 202;
    public static final int TYPE_CUSTOM = 203;

    public int fromPage=0;
    public int toPage=0;
    public int typeCode=-1;

    public String pdfName;
    public Uri uri;

    public ArrayList<Integer> selectedPages=new ArrayList<>();

    public PageSet(Uri uri){
        typeCode=TYPE_ALL;
        this.uri=uri;
        this.pdfName=new File(uri.getPath()).getName();
    }

    public PageSet(int fromPage, int toPage,String pdfName){
        this.typeCode=TYPE_RANGE;
        this.fromPage=fromPage;
        this.pdfName=pdfName;
        this.toPage=toPage;
    }

    public PageSet(ArrayList<Integer> selectedPages) {
        this.selectedPages=selectedPages;
    }

    public ArrayList<Integer> getSelectedPages() {
        return selectedPages;
    }
    public String getSelectedPagesString() {
        String txt="";
        for(int i:selectedPages){
            txt = txt + (i+1) + ", ";
        }
        if(txt.length()>2) {
            txt = txt.substring(0, txt.length() - 2);
        }
        return txt;
    }

}
