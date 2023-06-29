package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.adabter.ReviewAdapter;
import edu.birzeit.zamilihotal.adabter.RoomLayoutRecyclerViewAdapter;
import edu.birzeit.zamilihotal.database.DataBase;
import edu.birzeit.zamilihotal.model.Review;
import edu.birzeit.zamilihotal.model.User;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        SharedPreferences sp = this.getSharedPreferences("main", Context.MODE_PRIVATE);
        String target = sp.getString("target", "fake");

        if(target.equals("fake")) {
            FirebaseUser u = DataBase.auth.getCurrentUser();
            User user = new User(u.getEmail(), "123", "Mohammad", "Faraj", u.getPhoneNumber());
            List<Review> reviews = new ArrayList<>();
            reviews.add(new Review(user, "review", 1));
            reviews.add(new Review(user, "review", 1));
            reviews.add(new Review(user, "review", 1));
            reviews.add(new Review(user, "review", 1));
            reviews.add(new Review(user, "review", 1));
            reviews.add(new Review(user, "review", 1));

            RecyclerView recyclerView = findViewById(R.id.reviewsRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ReviewAdapter(this, reviews));
        }
    }

    public void profileClick(MenuItem item) {
    }
    public void BookingClick(MenuItem item) {
    }
    public void SearchClick(MenuItem item) {
    }
}
