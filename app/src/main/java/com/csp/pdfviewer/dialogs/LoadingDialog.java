package com.csp.pdfviewer.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog{

    public LoadingDialog(Context context) {
        super(context);
        super.setCancelable(false);
        super.setMessage("Loading...");
        super.setOnDismissListener(dialogInterface -> {
            super.setMessage("Loading...");
        });
    }

    public void show(String msg){
        super.setMessage(msg);
        show();
    }

}
