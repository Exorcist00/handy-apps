package com.emem.handyapps.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.emem.handyapps.R;

public class ImagePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            String fileName = bundle.getString("image");
            loadImage(fileName);
        }
    }

    private void loadImage(String fileName){

        ImageView image = (ImageView)findViewById(R.id.big_image);

        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        image.setImageBitmap(bitmap);
    }
}
