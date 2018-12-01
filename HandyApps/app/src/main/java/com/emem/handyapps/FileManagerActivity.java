package com.emem.handyapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emem.handyapps.gallery.ImagePreviewActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManagerActivity extends AppCompatActivity {

    private String pickedFolder = "";
    private List<String> filesList = new ArrayList<>();
    private String selectedFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        setButtonsEnabled();

        ListView filesListView = findViewById(R.id.filesList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filesList);
        filesListView.setAdapter(adapter);
        filesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tryToOpenFile(position);
                    }
                }
        );
        filesListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        markFile(position);
                        return true;
                    }
                });
    }

    public void pickFolder(View view){
        FolderPickerDialog folderPickerDialog = new FolderPickerDialog(this,
                new FolderPickerDialog.FolderPickerListener() {
                    @Override
                    public void onFolderPicked(String folder) {
                        updateFilesList(folder);
                    }
                });
        if (pickedFolder.equals(""))
            folderPickerDialog.pickFolder();
        else
            folderPickerDialog.pickFolder(pickedFolder);
    }

    private void setButtonsEnabled(){
        Button fileMove = findViewById(R.id.fileMove);
        fileMove.setEnabled(!selectedFile.equals(""));
        Button fileDelete = findViewById(R.id.fileDelete);
        fileDelete.setEnabled(!selectedFile.equals(""));
        Button fileNameChange = findViewById(R.id.fileNameChange);
        fileNameChange.setEnabled(!selectedFile.equals(""));
    }

    private void updateFilesList(String folder){
        pickedFolder = folder;
        Button folderPicker = (Button)findViewById(R.id.folderPicker);
        folderPicker.setText(pickedFolder);

        selectedFile = "";
        setButtonsEnabled();
        filesList = new ArrayList<>();
        File fileFolder = new File(folder);
        for (File file : fileFolder.listFiles()){
            if (file.isFile())
                filesList.add(file.getName());
        }
    }

    private void markFile(int position){
        selectedFile = filesList.get(position);

        setButtonsEnabled();
    }

    private void tryToOpenFile(int position){
        String fileName = filesList.get(position);
        String[] fileNameInParts = fileName.split("\\.");
        String extension = fileNameInParts[fileNameInParts.length-1];
        if ("pdf".equals(extension)){
            goToReader(pickedFolder + "/"  + fileName);
        }else if ("jpg".equals(extension)){
            goToImagePreview(pickedFolder + "/"  + fileName);
        }else if ("mp3".equals(extension)){
            //todo
        }else {
            Toast.makeText(this, "\"" + extension + "\" this file type is not supported",
                    Toast.LENGTH_SHORT).show();
        }
        selectedFile = "";
        setButtonsEnabled();
    }

    public void onFolderAdd(View view){
        choseNewFolderName();
        selectedFile = "";
        setButtonsEnabled();
    }

    public void onFileMove(View view){
        choseFolderToMoveFileIn();
    }

    public void onFileDelete(View view){
        deleteFile();
        selectedFile = "";
        setButtonsEnabled();
    }

    public void onFileNameChange(View view){
        choseNewNameForFile();
    }

    private void choseFolderToMoveFileIn(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What folder do you want to move file into?");
        final TextView input = new TextView(this);
        input.setText(pickedFolder);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveFile(input.getText().toString());
                selectedFile = "";
                setButtonsEnabled();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFile = "";
                setButtonsEnabled();
            }
        });
    }

    private void choseNewNameForFile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What new name should this file have?");
        final TextView input = new TextView(this);
        input.setText(selectedFile);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameFile(input.getText().toString());
                selectedFile = "";
                setButtonsEnabled();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFile = "";
                setButtonsEnabled();
            }
        });
    }

    private void choseNewFolderName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What name should this folder have?");
        final TextView input = new TextView(this);
        input.setText("");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createFolder(input.getText().toString());
            }
        });
    }

    private void moveFile(String newFolder){
        InputStream in = null;
        OutputStream out = null;
        try {
            File destination = new File(newFolder);
            if (!destination.exists()){
                destination.mkdirs();
            }

            in = new FileInputStream(pickedFolder + "/" + selectedFile);
            out = new FileOutputStream(newFolder + "/" + selectedFile);

            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();//save
            out.close();
            out = null;

            new File(pickedFolder + "/" + selectedFile).delete();
        }catch (FileNotFoundException e){
            Toast.makeText(this, "File doesn't exist",
                    Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Log.e("ERROR", e.getMessage());
        }
    }

    private void renameFile(String newName){
        InputStream in = null;
        OutputStream out = null;
        try {
            File name = new File(pickedFolder + "/" + newName);
            if (name.exists()){
                Toast.makeText(this, "File already exists",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            in = new FileInputStream(pickedFolder + "/" + selectedFile);
            out = new FileOutputStream(pickedFolder + "/" + newName);

            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();//save
            out.close();
            out = null;

            new File(pickedFolder + "/" + selectedFile).delete();
        }catch (FileNotFoundException e){
            Toast.makeText(this, "File doesn't exist",
                    Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Log.e("ERROR", e.getMessage());
        }
    }

    private void createFolder(String folderName){
        File path = new File(pickedFolder + "/" + folderName);
        if (!path.exists())
            path.mkdirs();
    }

    private void deleteFile(){
        new File(pickedFolder + "/" + selectedFile).delete();
    }

    private void goToReader(String fileName){
        Intent intent = new Intent(this, ReaderActivity.class);
        intent.putExtra("FileName", fileName);
        startActivity(intent);
    }

    private void goToImagePreview(String fileName){

        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("image", fileName);
        startActivity(intent);
    }
}
