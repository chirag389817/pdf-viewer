package com.csp.pdfviewer.utilclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.util.ArrayList;

public class Thumbnails {

    private static final String TAG = "Thumbnails";
    ArrayList<Bitmap> list=new ArrayList<>();

    Context context;
    Uri pdfUri;

    public Thumbnails(Context context, Uri pdfUri) {
        this.context = context;
        this.pdfUri = pdfUri;
        load();
    }

    private void load() {
        try {

            PdfiumCore pdfiumCore=new PdfiumCore(context);
            ParcelFileDescriptor pd=context.getContentResolver().openFileDescriptor(pdfUri,"r");

//            PdfDocument pdfDocument=pdfiumCore.newDocument(pd);
            PdfRenderer pdfRenderer = new PdfRenderer(pd);
            final int TOTAL_PAGES = pdfRenderer.getPageCount();
            Log.d(TAG, "load: page count "+TOTAL_PAGES);

            for(int pageIndex=0; pageIndex<TOTAL_PAGES; pageIndex++) {
                Log.d(TAG, "load: page"+pageIndex);
//                pdfiumCore.openPage(pdfDocument, pageIndex);
//                int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageIndex);
//                int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageIndex);
//                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageIndex, 0, 0, width, height);
//                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 150);
//                list.add(bitmap);
                PdfRenderer.Page page=pdfRenderer.openPage(pageIndex);
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(),page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                bitmap = ThumbnailUtils.extractThumbnail(bitmap,100,150);
                list.add(bitmap);
            }
//            pdfiumCore.closeDocument(pdfDocument);


        }catch (Exception obj){

            Log.d(TAG, "load: "+obj.toString());

        }
    }

    public int getCount(){
        return list.size();
    }

    public Bitmap get(int position){
        return list.get(position);
    }
}
