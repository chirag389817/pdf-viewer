package com.csp.pdfviewer.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import com.csp.pdfviewer.utilclasses.FileManager;
import com.csp.pdfviewer.utilclasses.Image;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImagesToPdf {

    private static final String TAG = "ImagesToPdf";

    Context context;

    public ImagesToPdf(Context context) {
        this.context = context;
        PDFBoxResourceLoader.init(context);
    }

    public File create(ArrayList<Image> imagesList, String name){
        try {
            PDDocument document = new PDDocument();
            for(Image image:imagesList){
                Log.d(TAG, "create: "+image.uri);
                Log.d(TAG, "create: "+FileManager.getPath(context,image.uri));
                PDPage page=new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream stream = new PDPageContentStream(document,page);

                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                image.bitmap.compress(Bitmap.CompressFormat.PNG,99,baos);
                byte[] byteArray = baos.toByteArray();
                PDImageXObject imageXObject=PDImageXObject.createFromByteArray(document,byteArray,"csp");

                float width= imageXObject.getWidth();
                float height= imageXObject.getHeight();
                float ratio = width/height;
                if(height>PDRectangle.A4.getHeight()){
                    height=PDRectangle.A4.getHeight();
                    width=ratio*height;
                }
                if(width>PDRectangle.A4.getWidth()){
                    width=PDRectangle.A4.getWidth();
                    height=width/ratio;
                }
                stream.drawImage(imageXObject,(PDRectangle.A4.getWidth()-(int)width)/2,(PDRectangle.A4.getHeight()-height)/2,width, height);
                stream.close();
            }
            File file = FileManager.getImagesTOPdfFile(name);
            document.save(file);
            document.close();
            return file;
        }catch (Exception obi){
            Log.e(TAG, "create: "+obi);
        }
        return null;
    }
}
