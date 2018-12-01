package com.emem.handyapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FolderPickerDialog {
    private String rootFolder = "";
    private Context context;
    private TextView titleView;
    private String folder = "";
    private List<String> subfolders = null;
    private FolderPickerListener folderPickerListener = null;
    private ArrayAdapter<String> folderListAdapter = null;

    public FolderPickerDialog(Context context, FolderPickerListener folderPickerListener){
        this.context = context;
        this.rootFolder = Environment.getRootDirectory().getAbsolutePath();
        this.folderPickerListener = folderPickerListener;

        try {
            this.rootFolder = new File(this.rootFolder).getCanonicalPath();
        }catch (IOException e){

        }
    }

    public void pickFolder(){
        pickFolder(this.rootFolder);
    }

    private boolean isThatFolderCorrect(String folder){
        File f = new File(folder);
        if (!f.exists() || !f.isDirectory())
            return false;
        return true;
    }

    private void navigateToSubFolder(DialogInterface dialog, int which){
        this.folder += "/" + ((AlertDialog) dialog).getListView().getAdapter().getItem(which);
        updateFolder();
    }

    private void onFolderPicked(){
        if (this.folderPickerListener != null){
            this.folderPickerListener.onFolderPicked(this.folder);
        }
    }

    private boolean onBackButton(){
        if (this.folder.equals(this.rootFolder))
            return false;
        else {
            this.folder = new File(this.folder).getParent();
            updateFolder();
            return true;
        }
    }

    public void pickFolder(String folder){
        if (!isThatFolderCorrect(folder))
            folder = this.rootFolder;

        try {
            folder = new File(folder).getCanonicalPath();
        }catch (IOException e) {
            return;
        }

        this.folder = folder;
        this.subfolders = getSubFolders(folder);

        AlertDialog.Builder dialogBuilder =
                createFolderPickerDialog(folder, this.subfolders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToSubFolder(dialog, which);
                    }
                });

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFolderPicked();
            }
        }).setNegativeButton("Cancel", null);

        final AlertDialog foldersDialog = dialogBuilder.create();

        foldersDialog.setOnKeyListener(
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        //if back button is pressed
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
                            return onBackButton();
                        else
                            return false;
                    }
                }
        );

        foldersDialog.show();
    }

    private List<String> getSubFolders(String folder){
        List<String> subfoldersList = new ArrayList<>();

        if (!isThatFolderCorrect(folder))
            return subfoldersList;

        File file = new File(folder);
        for (File f : file.listFiles()){
            if (f.isDirectory())
                subfoldersList.add(file.getName());
        }

        Collections.sort(subfoldersList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return subfoldersList;
    }

    private AlertDialog.Builder createFolderPickerDialog(String title, List<String> subfolders,
                                                         DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);

        LinearLayout titleLayout = new LinearLayout(this.context);
        titleLayout.setOrientation(LinearLayout.VERTICAL);

        this.titleView = new TextView(this.context);
        this.titleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.titleView.setTextAppearance(this.context, android.R.style.TextAppearance_Large);
        this.titleView.setTextColor(this.context.getResources().getColor(android.R.color.white));
        this.titleView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        this.titleView.setText(title);

        titleLayout.addView(this.titleView);

        dialogBuilder.setCustomTitle(titleLayout);

        this.folderListAdapter = createFolderListAdapter(subfolders);
        dialogBuilder.setSingleChoiceItems(this.folderListAdapter, -1, onClickListener);
        dialogBuilder.setCancelable(false);

        return dialogBuilder;
    }

    private void updateFolder(){
        this.subfolders.clear();
        this.subfolders.addAll(getSubFolders(this.folder));
        this.titleView.setText(this.folder);

        this.folderListAdapter.notifyDataSetChanged();
    }

    private ArrayAdapter<String> createFolderListAdapter(List<String> subfolders){
        return new ArrayAdapter<String>(this.context, android.R.layout.select_dialog_item, android.R.id.text1, subfolders){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (view instanceof TextView){
                    TextView textView = (TextView) view;
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    textView.setEllipsize(null);
                }
                return view;
            }
        };
    }


    public interface FolderPickerListener{
        public void onFolderPicked(String folder);
    }






























}
