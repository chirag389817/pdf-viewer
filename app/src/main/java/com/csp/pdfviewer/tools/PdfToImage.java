package com.csp.pdfviewer.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.pdfviewer.utilclasses.FileManager;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PdfToImage {

    private static final String TAG = "PdfToImage";
    private static final int TYPE_ALL = 501;
    private static final int TYPE_Range = 502;
    private static final int TYPE_CUSTOM = 503;

    Context context;
    OnCompleteListener completeListener;

    Uri pdfUri;
    int fromPage;
    int toPage;
    ArrayList<Integer> listPages;

    ArrayList<String> imagesList=new ArrayList<>();
    PDDocument document;
    PDFRenderer renderer;
    File dirToSave;

    public PdfToImage(Context context) {
        this.context = context;
        PDFBoxResourceLoader.init(context);
    }

    public void setOnCompleteListener(OnCompleteListener completeListener){
        this.completeListener=completeListener;
    }

    public void convert(Uri pdfUri,String dir){
        this.pdfUri=pdfUri;
        convert(dir,TYPE_ALL);
    }

    public void convert(Uri pdfUri,String dir, int fromPage, int toPage){
        this.pdfUri=pdfUri;
        this.fromPage=fromPage;
        this.toPage=toPage;
        convert(dir,TYPE_Range);
    }

    public void convert(Uri pdfUri,String dir, ArrayList<Integer> listPages){
        this.pdfUri=pdfUri;
        this.listPages=listPages;
        convert(dir,TYPE_CUSTOM);
    }

    private void convert(String dir,int type){
        new Handler().post(() -> {
            try{
                imagesList.clear();
                document=PDDocument.load(context.getContentResolver().openInputStream(pdfUri));
                renderer = new PDFRenderer(document);
                dirToSave=FileManager.makeDir(FileManager.PDF_TO_DIR,dir);
                if(type==TYPE_ALL) {
                    for (int i = 0; i < document.getNumberOfPages(); i++)
                        savePage(i);
                }else if(type==TYPE_Range && toPage>0) {
                    if(fromPage<1) fromPage=(1);
                    for(int i=fromPage-1; i<toPage && i<document.getNumberOfPages(); i++)
                        savePage(i);
                }
                else if(type==TYPE_CUSTOM){
                    for(int i: listPages)
                        savePage(i);
                }
                document.close();
                completeListener.onComplete(imagesList);
            }catch (Exception obj){
                Log.e(TAG, "convert: "+obj);
            }
        });
    }

    private void savePage(int pageIndex) throws IOException {
        Bitmap bitmap = renderer.renderImage(pageIndex);
        File file=FileManager.getPdfToImageFile(dirToSave,"page_"+(pageIndex+1));
        OutputStream fos=new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        fos.close();
        imagesList.add(file.getPath());
    }

    public interface OnCompleteListener{
        void onComplete(ArrayList<String> imagesList);
    }
}
