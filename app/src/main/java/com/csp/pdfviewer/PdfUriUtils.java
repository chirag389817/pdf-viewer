package com.csp.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;

public class PdfUriUtils {


    public static File getFile(Context context, Uri uri){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return new File(filePath);
    }

    @SuppressLint("Range")
    public static String getPdfName(Context context, Uri uri){
        String name=null;
        Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
        try{
            if(cursor!=null && cursor.moveToFirst())
                name=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
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
        return  name.substring(0,name.lastIndexOf("."));
    }

    public static Bitmap getThumnail(Context context, Uri pdfUri){
        PdfiumCore pdfiumCore=new PdfiumCore(context);
        try {
            ParcelFileDescriptor pd=context.getContentResolver().openFileDescriptor(pdfUri,"r");
            PdfDocument pdfDocument=pdfiumCore.newDocument(pd);
            pdfiumCore.openPage(pdfDocument,0);
            int width=pdfiumCore.getPageWidthPoint(pdfDocument,0);
            int height=pdfiumCore.getPageHeightPoint(pdfDocument,0);
            Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument,bitmap,0,0,0,width,height);
            pdfiumCore.closeDocument(pdfDocument);
            return  bitmap;
        }catch (Exception obj){
            Log.e("Thumbnail",obj.toString());
        }
        return null;
    }
}
