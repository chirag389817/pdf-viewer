package com.csp.pdfviewer.utilclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static final String SYSTEM_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final String STORAGE_DIR = SYSTEM_DIR + "/PDF Documents";
    public static final String SPLIT_DIR = STORAGE_DIR + "/Split";
    public static final String MERGE_DIR = STORAGE_DIR + "/Merge";

    public static int THUMBSIZE = 1024;

    public static File getFile(File file,String name){
        return new File(file.getPath()+"/"+name+".pdf");
    }

    public static File makeDir(String dir, String subDir){
        File file = new File(dir + "/" + subDir);
        if (file.mkdirs()) return file;
        else return makeDir(dir,subDir+"(1)");
    }

    public static String getSize(File file){
        String size = size=Long.toString(file.length());
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
        return size;
    }

    public static String getExtension(File file){
        String name = file.getName();
        return name.substring(name.lastIndexOf(".")+1,name.length());
    }

    public static Bitmap getThumbnail(Context context,File file){

        if(getExtension(file).equals("pdf")){
            return PdfInfo.getThumnail(context,Uri.fromFile(file));
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d( "getThumbnail: ",e.toString());
        }
        Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap,120,120);
        return thumbBitmap;
    }
}
