package com.csp.pdfviewer.tools;

import android.content.Context;
import android.util.Log;

import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.PageSet;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfMerger {

    private static final String TAG = "PdfMerger";

    Context context;

    public PdfMerger(Context context) {
        this.context = context;
        PDFBoxResourceLoader.init(context.getApplicationContext());
    }

    public File merge(ArrayList<PageSet> pageSetArrayList, String fileName){
        try {
            PDDocument newDocument=new PDDocument();
            PDFMergerUtility merger=new PDFMergerUtility();
            for (PageSet pageSet:pageSetArrayList){
                PDDocument document = PDDocument.load(context.getContentResolver().openInputStream(pageSet.uri));
                PDDocument docToAdd = new PDDocument();
                if(pageSet.typeCode==PageSet.TYPE_ALL){
                    Log.d(TAG, "merge: all");
                    for (int i=0; i<document.getNumberOfPages();i++) {
                        Log.d(TAG, "merge: "+i);
                        docToAdd.addPage(document.getPage(i));
                    }
                }else if(pageSet.typeCode==PageSet.TYPE_RANGE){
                    if(pageSet.fromPage==0 && pageSet.toPage==0) continue;
                    if(pageSet.fromPage==0) pageSet.fromPage=(1);
                    Log.d(TAG, "merge: from"+pageSet.fromPage);
                    Log.d(TAG, "merge: to"+pageSet.toPage);
                    for (int i=pageSet.fromPage-1; i<pageSet.toPage && i<document.getNumberOfPages(); i++) {
                        Log.d(TAG, "merge: "+i);
                        docToAdd.addPage(document.getPage(i));
                    }
                }else if(pageSet.typeCode==PageSet.TYPE_CUSTOM){
                    Log.d(TAG, "merge: custom");
                    for(int i:pageSet.selectedPages) {
                        Log.d(TAG, "merge: "+i);
                        newDocument.addPage(document.getPage(i));
                    }
                }
                merger.appendDocument(newDocument,docToAdd);
                docToAdd.close();
                document.close();
            }
            File mergedPdf = FileManager.getMergeFile(fileName);
            merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            if(newDocument.getNumberOfPages()>0){
                    newDocument.save(mergedPdf);
            }
            newDocument.close();
            return mergedPdf;
        }catch (Exception obj){
            Log.e(TAG, "merge: "+obj.toString());
        }
        return null;
    }

}
