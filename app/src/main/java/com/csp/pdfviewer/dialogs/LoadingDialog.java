package com.csp.pdfviewer.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog{

    public LoadingDialog(Context context) {
        super(context);
        super.setCancelable(false);
        super.setMessage("Loading...");
    }

    public LoadingDialog(Context context,String msg) {
        super(context);
        super.setCancelable(false);
        super.setMessage(msg);
    }

}
