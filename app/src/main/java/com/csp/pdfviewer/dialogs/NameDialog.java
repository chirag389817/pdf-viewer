package com.csp.pdfviewer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.csp.pdfviewer.databinding.DialogNameBinding;

public class NameDialog extends Dialog {

    DialogNameBinding binding;
    Context context;

    public NameDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        createDialog();
    }

    public NameDialog(@NonNull Context context,String title) {
        super(context);
        this.context=context;
        createDialog();
        setTitle(title);
    }

    private void createDialog() {
        binding = DialogNameBinding.inflate(getLayoutInflater());
        super.setContentView(binding.getRoot());
        super.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        super.setOnShowListener(dialogInterface -> {
            binding.editName.requestFocus();
        });
        binding.txtCancel.setOnClickListener(view -> super.dismiss());
    }

    public void setTitle(String title){
        binding.title.setText(title);
    }

    public void setOnConfirmListener(OnConfirmListener confirmer){
        binding.txtConfirm.setOnClickListener(view -> {
            String name = binding.editName.getText().toString();
            if(!name.equals("")){
                super.dismiss();
                confirmer.onConfirm(name);
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm(String name);
    }
}
