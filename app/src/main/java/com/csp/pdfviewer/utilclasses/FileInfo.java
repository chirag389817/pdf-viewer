package com.csp.pdfviewer.utilclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class FileInfo {

    public File file;
    public String info = "";
    public String name="";
    public Bitmap thumbnail;

    public FileInfo(Context context,String path) {
        file=new File(path);
        if(FileManager.getExtension(file).equals("pdf")){
            info=PdfInfo.getPageCount(context, Uri.fromFile(file))+" Pages ";
        }
        info=info+FileManager.getSize(file);
        name=file.getName();
        thumbnail=FileManager.getThumbnail(context,file);
    }

}
