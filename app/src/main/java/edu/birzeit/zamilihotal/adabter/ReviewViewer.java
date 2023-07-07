package edu.birzeit.zamilihotal.adabter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.birzeit.androidprojectzamili.R;

public class ReviewViewer extends RecyclerView.ViewHolder {


    TextView review_name;
    TextView review_text;
    TextView review_rate;

    public ReviewViewer(@NonNull View itemView) {
        super(itemView);
        review_name = itemView.findViewById(R.id.review_name);
        review_text = itemView.findViewById(R.id.review_text);
        review_rate = itemView.findViewById(R.id.review_rate);
    }

}
