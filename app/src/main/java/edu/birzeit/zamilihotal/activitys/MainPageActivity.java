package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.adabter.RoomLayoutRecyclerViewAdapter;
import edu.birzeit.zamilihotal.database.DataBase;
import edu.birzeit.zamilihotal.model.Hotel;
import edu.birzeit.zamilihotal.model.Review;
import edu.birzeit.zamilihotal.model.Room;
import edu.birzeit.zamilihotal.model.RoomType;
import edu.birzeit.zamilihotal.model.User;


public class MainPageActivity extends AppCompatActivity {

    Hotel hotel = new Hotel("Zamili Hotel", "Ramallah", "Palestine", 5);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);

        FirebaseUser user = DataBase.auth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainPageActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));
        roomList.add(new Room(hotel, 123, RoomType.DOUBLE_BED, null));


        Button logout = findViewById(R.id.logoutB);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase.auth.signOut();
                Intent i = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.RoomsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RoomLayoutRecyclerViewAdapter(this, roomList));
    }


    public void card_roomClicked(View v) {
        TextView textView = findViewById(R.id.room_card_title);
        String target = textView.getHint().toString();

        SharedPreferences sp = this.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("target", target);
        editor.commit();
        Intent intent = new Intent(MainPageActivity.this, RoomActivity.class);
        startActivity(intent);

    }
    public void profileClick(MenuItem item) {
    }
    public void BookingClick(MenuItem item) {
    }
    public void SearchClick(MenuItem item) {
    }
}
