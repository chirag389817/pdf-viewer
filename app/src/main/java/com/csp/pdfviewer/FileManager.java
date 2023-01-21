package com.csp.pdfviewer;

import android.os.Environment;

import java.io.File;

public class FileManager {

    public static final String SYSTEM_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final String STORAGE_DIR = SYSTEM_DIR + "/PDF Documents";
    public static final String SPLIT_DIR = STORAGE_DIR + "/Split";
    public static final String MERGE_DIR = STORAGE_DIR + "/Merge";

    public static File getFile(File file,String name){
        return new File(file.getPath()+"/"+name+".pdf");
    }

    public static File makeDir(String dir, String subDir){
        File file = new File(dir + "/" + subDir);
        if (file.mkdirs()) return file;
        else return makeDir(dir,subDir+"(1)");
    }
}
