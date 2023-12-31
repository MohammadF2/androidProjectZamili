package edu.birzeit.zamilihotal.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.Data.Public;
import edu.birzeit.zamilihotal.adabter.ImageAdapter;
import edu.birzeit.zamilihotal.adabter.ReviewAdapter;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Review;
import edu.birzeit.zamilihotal.model.Room;


public class RoomActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        SharedPreferences sp = this.getSharedPreferences("main", Context.MODE_PRIVATE);
        String target = sp.getString("target", "fake");
        Gson g = new Gson();
        Room room = g.fromJson(target, Room.class);
        FirebaseUser user = DataBase.auth.getCurrentUser();

        RecyclerView recyclerView = findViewById(R.id.imgRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageAdapter(this, room.getImages()));




        TextView roomTitle = findViewById(R.id.Room_title);
        ImageView firstImg = findViewById(R.id.firstImage);
        TextView desc = findViewById(R.id.desc);
        TextView subTitle = findViewById(R.id.subTitle);
        TextView price =  findViewById(R.id.room_price);
        TextView date = findViewById(R.id.room_date);
        TextView roomNo = findViewById(R.id.room_No);
        Button res = findViewById(R.id.resB);

        roomNo.setText("Room No: " + room.getRoomNo());
        roomTitle.setText(room.getRoomType());
        desc.setText(room.getDescription());
//            new DownloadImageTask(firstImg).execute(room.getImages().get(0).getImgURL());
        subTitle.setText(room.getRoomType());
        price.setText("Price only " + room.getPrice() + "$ a night");
        String startDate =sp.getString("startDate", "Jul 1 2023");
        date.setText( startDate + " - " + DateManager.getLastDate(startDate, sp.getInt("numberOfDays", 1)));

        Gson gson = new Gson();


        loadReviews(room.getRoomNo());


        res.setOnClickListener(v -> {

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Toast.makeText(RoomActivity.this, "A reservation has been added to you", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RoomActivity.this, SearchActivity.class);
                startActivity(intent);
            }, 2000);


            List<String> dates = Arrays.asList(gson.fromJson(sp.getString("targetedDates", "null"), String[].class));
            assert user != null;
            reserve(dates, room.getRoomNo(), user.getEmail());

        });
    }

    private void loadReviews(int roomNo) {

        String link = "https://mohammadf.site/Rest/getReviews.php?roomNo=" + roomNo;

        Log.d("link", link);
        StringRequest request = new StringRequest(Request.Method.GET, link, response -> {
            Gson gson = new Gson();
            Log.d("response", response);
            List<Review> reviews = Arrays.asList(gson.fromJson(response, Review[].class));
            RecyclerView recyclerView = findViewById(R.id.reviewsRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(RoomActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new ReviewAdapter(RoomActivity.this, reviews));
        }, error -> {

        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }


    int max;

    private void reserve(List<String> dates, int roomNo, String userEmail) {


        StringRequest request1 = new StringRequest(Request.Method.POST, "https://mohammadf.site/Rest/getMaxRes.php", response -> {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Log.d("jsonObject.getInt(\"MAX(id)\")", String.valueOf(jsonObject.getInt("MAX(id)")));
                max = jsonObject.getInt("MAX(id)") + 1;
            } catch (JSONException e) {
                max = 1;
            }
        }, error -> {

        });
        VolleySingleton.getInstance(this).addToRequestQueue(request1);


        Handler handler = new Handler();


        Log.d("dates", max + "");

        handler.postDelayed(() -> {
            for (int i = 0; i < dates.size(); i++) {
                String img_url = "https://mohammadf.site/Rest/addReservation.php";
                int finalI = i;
                StringRequest request = new StringRequest(Request.Method.POST, img_url,
                        response -> {
                        }, error -> Log.d("error", error.getMessage())) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String > rems = new HashMap<>();
                        rems.put("roomNo", Integer.toString(roomNo));
                        rems.put("userEmail", userEmail);
                        rems.put("Date", dates.get(finalI));
                        Log.d("String.valueOf(max)", String.valueOf(max));
                        rems.put("ReservationNum", String.valueOf(max));
                        return rems;
                    }
                };
                VolleySingleton.getInstance(Public.context).addToRequestQueue(request);
            }
        }, 700);
    }

    public void profileClick(MenuItem item) {
        Intent intent = new Intent(RoomActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(RoomActivity.this, BookingActivity.class);
        startActivity(intent);
    }
    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(RoomActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
