package com.emem.handyapps.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emem.handyapps.R;

import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>{
    private ArrayList<GalleryImage> galleryImages;
    private Context context;
    private View.OnClickListener onClickListener;

    public GalleryImageAdapter(Context context, ArrayList<GalleryImage> galleryImages, View.OnClickListener onClickListener) {
        this.galleryImages = galleryImages;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public GalleryImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryImageAdapter.ViewHolder holder, int position) {
        holder.title.setText(galleryImages.get(position).getName());
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmap = BitmapFactory.decodeFile(galleryImages.get(position).getPath());
        holder.image.setImageBitmap(bitmap);
        holder.image.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView image;

        public ViewHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.image_title);
            image = (ImageView)view.findViewById(R.id.image_image);
        }
    }
}
