package com.emem.handyapps.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.emem.handyapps.FolderPickerDialog;
import com.emem.handyapps.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String pickedFolder = "";
    private List<String> filesList;
    private RecyclerView gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gallery = (RecyclerView)findViewById(R.id.gallery);
        gallery.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gallery.setLayoutManager(layoutManager);
        filesList= new ArrayList<>();
        updateImages();
    }

    public void pickFolder(View view){
        FolderPickerDialog folderPickerDialog = new FolderPickerDialog(this,
                new FolderPickerDialog.FolderPickerListener() {
                    @Override
                    public void onFolderPicked(String folder) {
                        updateGallery(folder);
                    }
                });
        if (pickedFolder.equals(""))
            folderPickerDialog.pickFolder();
        else
            folderPickerDialog.pickFolder(pickedFolder);
    }

    private void updateGallery(String folder){
        pickedFolder = folder;
        Button galleryFolderPicker = (Button)findViewById(R.id.galleryFolderPicker);
        galleryFolderPicker.setText(pickedFolder);
        filesList= new ArrayList<>();
        File fileFolder = new File(folder);
        for (File file : fileFolder.listFiles()){
            if (file.isFile()) {
                String[] fileNameInParts = file.getName().split("\\.");
                String extension = fileNameInParts[fileNameInParts.length-1];
                if ("jpg".equals(extension))
                    filesList.add(file.getName());
            }
        }
        updateImages();
    }

    private void updateImages(){
        ArrayList<GalleryImage> images = prepareImages();
        GalleryImageAdapter adapter = new GalleryImageAdapter(getApplicationContext(), images,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onImageClick(v);
                    }
                });
        gallery.setAdapter(adapter);
    }

    private ArrayList<GalleryImage> prepareImages(){
        ArrayList<GalleryImage> images = new ArrayList<>();
        for (String name : filesList){
            GalleryImage image = new GalleryImage();
            image.setName(name);
            image.setPath(pickedFolder + "/" + name);
            images.add(image);
        }
        return images;
    }

    private void onImageClick(View v){
        int pos = gallery.getChildLayoutPosition(v);

        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("image", pickedFolder + "/" + filesList.get(pos));
        startActivity(intent);
    }

}
