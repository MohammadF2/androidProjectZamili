package edu.birzeit.zamilihotal.adabter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.model.Review;


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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewer holder, int position) {
        holder.review_name.setText(reviews.get(position).getUserEmail());
        holder.review_text.setText(reviews.get(position).getReview());
        holder.review_rate.setText(reviews.get(position).getRate() + "/5");
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
