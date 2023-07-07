package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.adabter.RoomLayoutRecyclerViewAdapter;
import edu.birzeit.zamilihotal.model.Room;


public class RoomMenuActivity extends AppCompatActivity {

    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_menu);

        SharedPreferences sp = RoomMenuActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        FirebaseUser user = DataBase.auth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(RoomMenuActivity.this, MainActivity.class);
            editor.remove("currUser");
            editor.apply();
            startActivity(i);
            finish();
        }




        List<Room> roomList = Arrays.asList(gson.fromJson(sp.getString("roomsToShow", "nan"), Room[].class));

        Button logout = findViewById(R.id.logoutB);
        logout.setOnClickListener(v -> {
            DataBase.auth.signOut();
            editor.remove("currUser");
            editor.apply();
            Intent i = new Intent(RoomMenuActivity.this, MainActivity.class);
            startActivity(i);
        });





        RecyclerView recyclerView = findViewById(R.id.RoomsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RoomLayoutRecyclerViewAdapter(this, roomList));
    }
    public void profileClick(MenuItem item) {
        Intent intent = new Intent(RoomMenuActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(RoomMenuActivity.this, BookingActivity.class);
        startActivity(intent);
    }
    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(RoomMenuActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
