package edu.birzeit.zamilihotal.activitys;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.Data.Public;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.adabter.SpinnerAdapter;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Image;
import edu.birzeit.zamilihotal.model.Reservation;
import edu.birzeit.zamilihotal.model.Room;
import edu.birzeit.zamilihotal.model.RoomType;
import edu.birzeit.zamilihotal.model.SpinnerItem;

public class SearchActivity extends AppCompatActivity {


    DatePickerDialog datePickerDialog;
    Button datePicker;
    List<Room> roomList = new ArrayList<>();
    EditText number;
    List<Reservation> reservations = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Public.context = this;
        List<SpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_child_24, RoomType.SINGLE_BED));
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_parent_24, RoomType.DOUBLE_BED));
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_parent_24, RoomType.LARGE_BED));
        SpinnerAdapter adapter1 = new SpinnerAdapter(this, spinnerItems);
        Spinner spinner = findViewById(R.id.type_spinner);
        spinner.setAdapter(adapter1);

        setDatePickerDialog();

        datePicker = findViewById(R.id.date_picker_actions);
        datePicker.setText(DateManager.getTodayDate());

        datePicker.setOnClickListener(v -> datePickerDialog.show());

        number = findViewById(R.id.number_of_days);
        Button search = findViewById(R.id.searchB);
        TextView errorTxt = findViewById(R.id.errorSearch);

        search.setOnClickListener(v -> {

            TextView textView = findViewById(R.id.searchMessage);
            textView.setText("Searching for rooms please wait");

            Log.d("isSelected", spinner.isSelected()+"");
            if(number.getText().toString().equals("")) {
                errorTxt.setText("You must choose a number");
                return;
            }

          getReservations();
          Handler handler = new Handler();
          handler.postDelayed(() -> getRooms(((SpinnerItem) spinner.getSelectedItem()).getText()), 300);

        });

    }


    private void getReservations() {

        Gson gson = new Gson();
        String img_url = "https://mohammadf.site/Rest/getReservations.php";
        StringRequest request = new StringRequest(Request.Method.POST, img_url,
                response -> {
                    Reservation[] reservationsArr = gson.fromJson(response, Reservation[].class);
                    reservations = Arrays.asList(reservationsArr);
                    Log.d("reservations 1", reservations.size() + "");
                }, error -> Log.d("error", error.getMessage()));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    List<Image> images = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    private void getRooms(String roomType) {
        TextView textView = findViewById(R.id.searchMessage);
        textView.setText("Searching for rooms please wait");

        Gson gson = new Gson();
        String img_url = "https://mohammadf.site/Rest/getRoomsType.php?roomType=" + roomType;
        StringRequest request = new StringRequest(Request.Method.GET, img_url,
                response -> {
                    Room[] roomsArr = gson.fromJson(response, Room[].class);
                    roomList = Arrays.asList(roomsArr);

                    List<String> dates = DateManager.getNextNDays(datePicker.getText().toString(), Integer.parseInt(number.getText().toString()));
                    SharedPreferences sp = SearchActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("startDate", datePicker.getText().toString());
                    editor.putInt("numberOfDays", Integer.parseInt(number.getText().toString()));



                    List<Room> filteredRooms = new ArrayList<>();



                    for (Room room : roomList) {
                        boolean isReserved = false;
                        for (Reservation reservation : reservations) {
                            if (reservation.getRoomNo() == room.getRoomNo()) {
                                for (String date :dates) {
                                    if(date.equals(reservation.getDate())) {
                                        isReserved = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!isReserved) {
                            filteredRooms.add(room);
                        }
                    }

                    Log.d("filteredRooms", filteredRooms.size() + "");

                    for (int i = 0; i < filteredRooms.size(); i++) {
                        String img_url1 = "https://mohammadf.site/Rest/getRoomImg.php";
                        int finalI = i;
                        StringRequest request1 = new StringRequest(Request.Method.POST, img_url1,
                                response1 -> {
                                    Log.d("img", response1);
                                    try {
                                        Image[] imgArr = gson.fromJson(response1, Image[].class);
                                        images = Arrays.asList(imgArr);
                                        filteredRooms.get(finalI).setImages(images);
                                    } catch (Exception ignored) {

                                    }
                                }, error -> Log.d("error", error.getMessage())) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String > rems = new HashMap<>();
                                Log.d("roomNo", Integer.toString(filteredRooms.get(finalI).getRoomNo()));
                                rems.put("roomNo", Integer.toString(filteredRooms.get(finalI).getRoomNo()));
                                return rems;
                            }
                        };
                        VolleySingleton.getInstance(Public.context).addToRequestQueue(request1);
                    }



                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        if(filteredRooms.size() == 0){
                            Toast.makeText(SearchActivity.this, "No rooms available", Toast.LENGTH_SHORT).show();
                            textView.setText("No rooms available");
                        } else {
                            Gson gson1 = new Gson();
                            editor.putString("roomsToShow", gson1.toJson(filteredRooms.toArray()));
                            editor.putString("targetedDates", gson1.toJson(dates.toArray()));
                            textView.setText("");
                            editor.apply();
                            Intent intent = new Intent(SearchActivity.this, RoomMenuActivity.class);
                            startActivity(intent);
                        }
                    }, 3000);

                }, error -> Log.d("error", error.getMessage()));
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month +1;
            String date = DateManager.makeDateString(day, month, year);
            datePicker.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    public void profileClick(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, BookingActivity.class);
        startActivity(intent);
    }
    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
