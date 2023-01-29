package com.csp.pdfviewer.utilclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static final String SYSTEM_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final String STORAGE_DIR = SYSTEM_DIR + "/PDF Documents";
    public static final String SPLIT_DIR = STORAGE_DIR + "/Split";
    public static final String MERGE_DIR = STORAGE_DIR + "/Merge";
    public static final String IMG_TO_DIR = STORAGE_DIR + "/Images to PDF";

    public static int THUMBSIZE = 1024;

    public static File getFile(File file,String name){
        return new File(file.getPath()+"/"+name+".pdf");
    }

    public static File makeDir(String dir, String subDir){
        File file = new File(dir + "/" + subDir);
        if (file.mkdirs()) return file;
        else return makeDir(dir,subDir+"(1)");
    }

    public static File getMergeFile(String fileName){
        new File(MERGE_DIR).mkdirs();
        return new File(MERGE_DIR+"/"+fileName+".pdf");
    }

    public static File getImagesTOPdfFile(String fileName){
        new File(IMG_TO_DIR).mkdirs();
        return new File(IMG_TO_DIR+"/"+fileName+".pdf");
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

    @SuppressLint("Range")
    public static String getName(Context context, Uri uri){
        String name = null;
        Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
        try{
            if(cursor!=null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
            if (cursor != null) {
                cursor.close();
            }
        }catch (Exception obj){
            Log.e("FileManager", "loadInfo: "+obj.toString());
        }
        return name;
    }

    public static String getPath(Context context,Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,filePathColumn,null,null,null);
        if(cursor.moveToFirst()){
            int columIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path= cursor.getString(columIndex);
            cursor.close();
            return path;
        }
        return "ni maylu";
    }
}
