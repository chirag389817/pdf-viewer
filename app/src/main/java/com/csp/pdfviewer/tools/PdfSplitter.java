package com.csp.pdfviewer.tools;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PageSet;
import com.csp.pdfviewer.utilclasses.PdfInfo;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.ArrayList;

public class PdfSplitter{

    private final String TAG = "PdfSplitterThread";

    Context context;
    PdfInfo pdfToSplit;
    ArrayList<PageSet> pageSetArrayList;
    public ArrayList<String> splitedPdfs=new ArrayList<>();
    File dirToSave;


    public PdfSplitter(Context context){
        this.context=context;
        PDFBoxResourceLoader.init(context.getApplicationContext());
    }

    public void split(PdfInfo pdfToSplit, ArrayList<PageSet> pageSetArrayList){
        this.pdfToSplit=pdfToSplit;
        this.pageSetArrayList=pageSetArrayList;
        splitedPdfs.clear();
        run();
    }

    public void run() {
        Log.d(TAG,"runing...");

        try {
            PDDocument document = PDDocument.load(context.getContentResolver().openInputStream(pdfToSplit.uri));
            dirToSave = FileManager.makeDir(FileManager.SPLIT_DIR,pdfToSplit.name);
            for(PageSet pageSet : pageSetArrayList){
                File fileToSave = FileManager.getFile(dirToSave,pageSet.pdfName);
                PDDocument newDocument=new PDDocument();
                if(pageSet.typeCode==PageSet.TYPE_ALL){
                    newDocument=document;
                }else if(pageSet.typeCode==PageSet.TYPE_RANGE){
                    if(pageSet.fromPage==0 && pageSet.toPage==0) continue;
                    if(pageSet.fromPage==0) pageSet.fromPage=(1);
                    for (int page=pageSet.fromPage-1; page<pageSet.toPage && page<pdfToSplit.pageCount; page++){
                        newDocument.addPage(document.getPage(page));
                    }
                }else if(pageSet.typeCode==PageSet.TYPE_CUSTOM){
                    for (int page: pageSet.getSelectedPages()){
                        newDocument.addPage(document.getPage(page));
                    }
                }else {
                    Log.e(TAG,Integer.toString(pageSet.typeCode));
                    return;
                }
                if(newDocument.getNumberOfPages()>0){
                    newDocument.save(fileToSave);
                    splitedPdfs.add(fileToSave.getPath());
                }
                if(newDocument!=document){
                    newDocument.close();
                }
                Log.d(TAG,"splited "+pageSet.pdfName);
            }
            document.close();
        }catch (Exception obj){
            Log.e(TAG,obj.toString());
        }
    }

}
