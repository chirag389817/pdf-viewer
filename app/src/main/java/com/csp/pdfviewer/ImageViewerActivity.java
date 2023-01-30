package com.csp.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.csp.pdfviewer.adapters.RAImageViewer;
import com.csp.pdfviewer.databinding.ActivityImageViewerBinding;
import com.csp.pdfviewer.databinding.ActivityPdfToImageBinding;
import com.csp.pdfviewer.utilclasses.ACBar;
import com.csp.pdfviewer.utilclasses.Image;
import com.csp.pdfviewer.utilclasses.Launcher;

import java.io.File;
import java.util.ArrayList;

public class ImageViewerActivity extends AppCompatActivity {

    public static final String TYPE = "image_viewer_data_type";
    public static final String PATH_LIST = "image_viewer_data_type_string_list";
    public static final String LIST_KEY = "image_viewer_data_list_key";
    public static final String POSITION = "initial_position";

    ActivityImageViewerBinding binding;
    ArrayList<Image> listImages=new ArrayList<>();
    RAImageViewer adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ACBar.setActionBar(this,binding.getRoot());

        loadImages();
        setUpViewPager();
    }

    private void setUpViewPager() {
        adapter=new RAImageViewer(listImages);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(getIntent().getIntExtra(POSITION,0));
    }

    private void loadImages() {
        if(getIntent().getStringExtra(TYPE).equals(PATH_LIST)){
            for (String path:getIntent().getStringArrayListExtra(LIST_KEY))
                listImages.add(new Image(this,Uri.fromFile(new File(path))));
        }else{
            for(Uri uri: Launcher.getUriData(getIntent()))
                listImages.add(new Image(this,uri));
        }
    }
}