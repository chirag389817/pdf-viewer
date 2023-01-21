package com.csp.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileNotFoundException;

public class PdfInfo {

    public Uri uri;
    public String name="";
    public Context context;
    public String size="-1";
    public int pageCount=-1;
    public File file;

    public PdfInfo(Context context,Uri uri){
        this.context=context;
        this.uri=uri;
        file=new File(uri.getPath());
        loadInfo();
    }

    @SuppressLint("Range")
    public void loadInfo(){
        Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
        try{
            if(cursor!=null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                size = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE));
                int sizeInt=Integer.parseInt(size);
                sizeInt/=1024;
                if (sizeInt<1024){
                    size=Integer.toString(sizeInt,0)+" KB";
                }else{
                    float sizeFloat=sizeInt/1024.0f;
                    size=Float.toString(sizeFloat);
                    size=size.substring(0,size.lastIndexOf(".")+3);
                    size=size+" MB";
                }
            }
        }finally {
            cursor.close();
        }
        if (name == null) {
            name = uri.getPath();
            int cut = name.lastIndexOf('/');
            if (cut != -1) {
                name = name.substring(cut + 1);
            }
        }
        name=name.substring(0,name.lastIndexOf("."));

        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri,"r");
            PdfRenderer pdfRenderer = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                pageCount = pdfRenderer.getPageCount();
                pdfRenderer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getThumnail(int pageIndex){
        PdfiumCore pdfiumCore=new PdfiumCore(context);
        try {
            ParcelFileDescriptor pd=context.getContentResolver().openFileDescriptor(uri,"r");
            PdfDocument pdfDocument=pdfiumCore.newDocument(pd);
            pdfiumCore.openPage(pdfDocument,pageIndex);
            int width=pdfiumCore.getPageWidthPoint(pdfDocument,pageIndex);
            int height=pdfiumCore.getPageHeightPoint(pdfDocument,pageIndex);
            Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument,bitmap,pageIndex,0,0,width,height);
            pdfiumCore.closeDocument(pdfDocument);
            return  bitmap;
        }catch (Exception obj){
            Log.e("Thumbnail",obj.toString());
        }
        return null;
    }
}
