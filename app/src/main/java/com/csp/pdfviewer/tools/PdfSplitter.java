package com.csp.pdfviewer.tools;

import android.content.Context;
import android.util.Log;

import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PageSet;
import com.csp.pdfviewer.utilclasses.PdfInfo;
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
                File fileToSave = FileManager.getFile(dirToSave,pageSet.getPdfName());
                PDDocument newDocument=new PDDocument();
                if(pageSet.getTypeCode()==PageSet.TYPE_ALL){
                    newDocument=document;
                }else if(pageSet.getTypeCode()==PageSet.TYPE_RANGE){
                    if(pageSet.getFromPage()==0 && pageSet.getToPage()==0) continue;
                    if(pageSet.getFromPage()==0) pageSet.setFromPage(1);
                    for (int page=pageSet.getFromPage()-1; page<pageSet.getToPage() && page<pdfToSplit.pageCount; page++){
                        newDocument.addPage(document.getPage(page));
                    }
                }else if(pageSet.getTypeCode()==PageSet.TYPE_CUSTOM){

                }else {
                    Log.e(TAG,Integer.toString(pageSet.getTypeCode()));
                    return;
                }
                newDocument.save(fileToSave);
                if(newDocument!=document){
                    newDocument.close();
                }
                splitedPdfs.add(fileToSave.getPath());
                Log.d(TAG,"splited "+pageSet.getPdfName());
            }
            document.close();
        }catch (Exception obj){
            Log.e(TAG,obj.toString());
        }
    }

}
