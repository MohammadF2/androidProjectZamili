package edu.birzeit.zamilihotal.adabter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.controllers.DownloadImageTask;
import edu.birzeit.zamilihotal.model.Image;


public class ImageAdapter extends RecyclerView.Adapter<ImageViewer>{


    Context context;
    List<Image> images;


    public ImageAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewer(LayoutInflater.from(context).inflate(R.layout.img_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewer holder, int position) {
        new DownloadImageTask(holder.img).execute(images.get(position).getImgURL());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
