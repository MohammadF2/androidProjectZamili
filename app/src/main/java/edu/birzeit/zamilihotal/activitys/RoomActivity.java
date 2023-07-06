package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.Public;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.adabter.ImageAdapter;
import edu.birzeit.zamilihotal.adabter.ReviewAdapter;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.DownloadImageTask;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.model.Image;
import edu.birzeit.zamilihotal.model.Review;
import edu.birzeit.zamilihotal.model.Room;
import edu.birzeit.zamilihotal.model.User;

public class RoomActivity extends AppCompatActivity {

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
        Button res = findViewById(R.id.resB);

        roomTitle.setText(room.getRoomType());
        desc.setText(room.getDescription());
//            new DownloadImageTask(firstImg).execute(room.getImages().get(0).getImgURL());
        subTitle.setText(room.getRoomType());
        price.setText("Price only " + room.getPrice() + "$ a night");
        String startDate =sp.getString("startDate", "Jul 1 2023");
        date.setText( startDate + " - " + DateManager.getLastDate(startDate, sp.getInt("numberOfDays", 1)));

        Gson gson = new Gson();

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RoomActivity.this, "A reservation has been added to you", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RoomActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                }, 2000);


                List<String> dates = Arrays.asList(gson.fromJson(sp.getString("targetedDates", "null"), String[].class));
                reserve(dates, room.getRoomNo(), user.getEmail());

            }
        });

    }


    int max;

    private void reserve(List<String> dates, int roomNo, String userEmail) {

        RequestQueue queue = Volley.newRequestQueue(Public.context);
        StringRequest request1 = new StringRequest(Request.Method.POST, "https://mohammadf.site/Rest/getMaxRes.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d("jsonObject.getInt(\"MAX(id)\")", String.valueOf(jsonObject.getInt("MAX(id)")));
                    max = jsonObject.getInt("MAX(id)") + 1;
                } catch (JSONException e) {
                    max = 1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request1);

        Handler handler = new Handler();


        Log.d("dates", max + "");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < dates.size(); i++) {
                    String img_url = "https://mohammadf.site/Rest/addReservation.php";
                    int finalI = i;
                    StringRequest request = new StringRequest(Request.Method.POST, img_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String > prems = new HashMap<>();
                            prems.put("roomNo", Integer.toString(roomNo));
                            prems.put("userEmail", userEmail);
                            prems.put("Date", dates.get(finalI));
                            Log.d("String.valueOf(max)", String.valueOf(max));
                            prems.put("ReservationNum", String.valueOf(max));
                            return prems;
                        }
                    };
                    queue.add(request);
                }
            }
        }, 700);
    }

    public void profileClick(MenuItem item) {
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
