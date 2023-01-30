package com.csp.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.csp.pdfviewer.databinding.ActivityPdfToImageBinding;
import com.csp.pdfviewer.dialogs.LoadingDialog;
import com.csp.pdfviewer.tools.PdfToImage;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Launcher;
import com.csp.pdfviewer.utilclasses.PageSet;
import com.csp.pdfviewer.utilclasses.PdfInfo;

import java.util.ArrayList;
import java.util.Arrays;

public class PdfToImageActivity extends AppCompatActivity {

    ActivityPdfToImageBinding binding;
    PdfInfo pdfInfo;
    PdfToImage pdfToImage;
    PageSet pageSet;
    Intent pdfOpner = new Intent(this,PdfViewerActivity.class);
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPdfToImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ACBar.setActionBar(this,binding.getRoot(),"PDF to Images");
        loadingDialog=new LoadingDialog(this,"Converting");

        loadPdfDetails();
        setSpinner();
        setConverter();
        selClickListeners();
    }

    private void setConverter() {
        pdfToImage=new PdfToImage(this);
        pdfToImage.setOnCompleteListener(imagesList -> {
            Intent resultShower=new Intent(this,ResultActivity.class);
            resultShower.putStringArrayListExtra(ResultActivity.LIST_KEY,imagesList);
            loadingDialog.dismiss();
            startActivity(resultShower);
            finish();
        });
    }

    private void selClickListeners() {
        binding.cardSelectPages.setOnClickListener(view -> {
            Intent pageSelector=new Intent(this,SelectPagesActivity.class);
            pageSelector.setData(pdfInfo.uri);
            pageSelector.putIntegerArrayListExtra(SelectPagesActivity.LIST_KEY,pageSet.getSelectedPages());
            resultLauncher.launch(pageSelector);
        });
        binding.cardView.setOnClickListener(view -> {
            pdfOpner.setData(pdfInfo.uri);
            startActivity(pdfOpner);
        });
        binding.cardConvert.setOnClickListener(view -> {
            loadingDialog.show();
            int position = binding.spinPSType.getSelectedItemPosition();
            if(position==0) {
                pdfToImage.convert(pdfInfo.uri,pdfInfo.name);
            }else if(position==1) {
                int fromPage=0,toPage=0;
                String strFrom=binding.editFrom.getText().toString();
                String strTo=binding.editTo.getText().toString();
                if(!strFrom.equals("")) fromPage=Integer.parseInt(strFrom);
                if(!strTo.equals("")) toPage=Integer.parseInt(strTo);
                pdfToImage.convert(pdfInfo.uri,pdfInfo.name, fromPage,toPage);
            }else if(position==2){
                pdfToImage.convert(pdfInfo.uri, pdfInfo.name,pageSet.getSelectedPages());
            }
        });
    }

    private void setSpinner() {
        ArrayList<String> spinnerItems=new ArrayList<>(Arrays.asList("All","Range","Custom"));
        binding.spinPSType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerItems));
        binding.spinPSType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==1){
                    binding.linRange.setVisibility(View.VISIBLE);
                    binding.linCustom.setVisibility(View.GONE);
                }else if(position==2){
                    binding.linCustom.setVisibility(View.VISIBLE);
                    binding.linRange.setVisibility(View.GONE);
                }else {
                    binding.linCustom.setVisibility(View.GONE);
                    binding.linRange.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinPSType.setSelection(0);
    }

    @SuppressLint("SetTextI18n")
    private void loadPdfDetails() {
        pdfInfo = new PdfInfo(this, Launcher.getUriData(getIntent()).get(0));
        pageSet=new PageSet(pdfInfo.uri);
        binding.txtPdfName.setText(pdfInfo.name);
        binding.txtPdfInfo.setText(pdfInfo.pageCount+" Pages "+pdfInfo.size);
        binding.imgThumbnail.setImageBitmap(pdfInfo.getPageThumnail(0));
    }

    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                    pageSet=new PageSet(result.getData().getIntegerArrayListExtra(SelectPagesActivity.LIST_KEY));
                    binding.txtCustomPages.setText(pageSet.getSelectedPagesString());
                }
            }
    );
}