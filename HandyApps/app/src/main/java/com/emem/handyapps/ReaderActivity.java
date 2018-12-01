package com.emem.handyapps;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.nio.ByteBuffer;

public class ReaderActivity extends AppCompatActivity implements OnPageChangeListener {

    private boolean changeColor = false;
    private String fileName="";
    private PDFView pdfView;
    private Integer pageNo = 0;
    private int brightness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        //todo ? checkWriteSettingsPermission();
        loadBrightness();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            fileName = bundle.getString("FileName");
            //todo load page no
            loadPdf(fileName, changeColor);
        }

    }

    private void loadPdf(String fileName, boolean changeColor){
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        int size;
        if (Build.VERSION.SDK_INT>=19)
            size = bitmap.getAllocationByteCount();
        else
            size = bitmap.getRowBytes() * bitmap.getHeight();

        if (changeColor) {
            int[] array = new int[size];
            bitmap.getPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            for (int i = 0; i < size; i++) {
                if (array[i] == Color.BLACK)
                    array[i] = Color.GREEN;
            }
            bitmap.setPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);

        byte[] bytes = byteBuffer.array();

        displayPDF(bytes);
    }

    private void displayPDF(byte[] bytes){
        pdfView.fromBytes(bytes)
                .defaultPage(pageNo)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    public void changeColor(View view){
        changeColor = !changeColor;
        loadPdf(fileName, changeColor);
    }

    public void brightnessUp(View view){
        brightness +=10;//0-255
        if (brightness>255)
            brightness = 255;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness/(float) 255;
        getWindow().setAttributes(lp);
    }

    public void brightnessDown(View view){
        brightness -=10;//0-255
        if (brightness<0)
            brightness = 0;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness/(float) 255;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNo = page;
        //todo save page
    }

    private void loadBrightness(){
        try {
            ContentResolver contentResolver = getContentResolver();
            Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        }catch (Settings.SettingNotFoundException e){
            Log.e("ERROR", "Setting not found");
            e.printStackTrace();
        }
    }

    private void restoreBrightness(){

    }

    private void checkWriteSettingsPermission(){
        if (Build.VERSION.SDK_INT>=23) {
            boolean canWrite = Settings.System.canWrite(getApplicationContext());
            if (!canWrite) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        restoreBrightness();
    }
}
