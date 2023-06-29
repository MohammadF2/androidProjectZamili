package edu.birzeit.zamilihotal.adabter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.model.Review;
import edu.birzeit.zamilihotal.model.Room;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewer>{

    Context context;
    List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewer(LayoutInflater.from(context).inflate(R.layout.review_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewer holder, int position) {
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
