package com.csp.pdfviewer.utilclasses;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import java.util.ArrayList;

public class Launcher{

    public static ActivityResultLauncher<Intent> create(Context context, OnResultOk onResultOk){
        return ((AppCompatActivity)context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK && result.getData()!=null){
                        onResultOk.resultOk(getUriData(result.getData()));
                    }
                }
        );
    }

    public static ArrayList<Uri> getUriData(@NonNull Intent intent){
        ArrayList<Uri> listUri = new ArrayList<>();
        Uri uri=intent.getData();
        if(uri!=null)
            listUri.add(uri);
        ClipData clipData=intent.getClipData();
        if(clipData!=null)
            for (int i=0; i<clipData.getItemCount(); i++)
                listUri.add(clipData.getItemAt(i).getUri());
        return listUri;
    }

    public static Intent createPicker(String mimeType, boolean allow_multiple){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,allow_multiple);
        return Intent.createChooser(intent,"Select");
    };

    public interface OnResultOk{
        void resultOk(ArrayList<Uri> listUri);
    }

}
