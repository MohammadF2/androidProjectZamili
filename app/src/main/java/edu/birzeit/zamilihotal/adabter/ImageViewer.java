package edu.birzeit.zamilihotal.adabter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.birzeit.androidprojectzamili.R;

public class ImageViewer extends RecyclerView.ViewHolder{
    ImageView img;
    public ImageViewer(@NonNull View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.img);
    }
}
