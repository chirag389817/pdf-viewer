package com.csp.pdfviewer.utilclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Image {

    private static final String TAG = "Image";

    public Uri uri;
    public Bitmap bitmap;

    public Image(Context context,Uri uri) {
        this.uri = uri;
        try {
            InputStream stream=context.getContentResolver().openInputStream(uri);
            bitmap=BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Image: "+e);
        }
    }

}
