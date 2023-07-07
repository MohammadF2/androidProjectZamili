package edu.birzeit.zamilihotal.activitys;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import edu.birzeit.zamilihotal.adabter.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Public.context = this;
        String[] types = {RoomType.DOUBLE_BED, RoomType.SINGLE_BED, RoomType.LARGE_BED};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style,types) {
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                return v;
            }
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };

        List<SpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_child_24, RoomType.SINGLE_BED));
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_parent_24, RoomType.DOUBLE_BED));
        spinnerItems.add(new SpinnerItem(R.drawable.baseline_bedroom_parent_24, RoomType.LARGE_BED));

        SpinnerAdapter adapter1 = new SpinnerAdapter(this, spinnerItems);

        Spinner spinner = findViewById(R.id.type_spinner);
        spinner.setAdapter(adapter1);

        Button logout = findViewById(R.id.logoutB_search);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = SearchActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                DataBase.auth.signOut();
                editor.remove("currUser");
                editor.commit();
                Intent i = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(i);
            }
        });




        setDatePickerDialog();

        datePicker = findViewById(R.id.date_picker_actions);
        datePicker.setText(DateManager.getTodayDate());

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        number = findViewById(R.id.number_of_days);
        Button search = findViewById(R.id.searchB);
        TextView errorTxt = findViewById(R.id.errorSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = findViewById(R.id.searchMessage);
                textView.setText("Searching for rooms please wait");

                Log.d("isSelected", spinner.isSelected()+"");
                if(number.getText().toString().equals("")) {
                    errorTxt.setText("You must choose a number");
                    return;
                }


              getReservations();
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      getRooms(((SpinnerItem) spinner.getSelectedItem()).getText());
                  }
              }, 300);

            }
        });

    }


    private void getReservations() {

        Gson gson = new Gson();
        String img_url = "https://mohammadf.site/Rest/getReservations.php";
        StringRequest request = new StringRequest(Request.Method.POST, img_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Reservation[] reservationsArr = gson.fromJson(response, Reservation[].class);
                        reservations = Arrays.asList(reservationsArr);
                        Log.d("reservations 1", reservations.size() + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.getMessage());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    List<Image> images = new ArrayList<>();

    private void getRooms(String roomType) {
        TextView textView = findViewById(R.id.searchMessage);
        textView.setText("Searching for rooms please wait");

        Gson gson = new Gson();
        String img_url = "https://mohammadf.site/Rest/getRoomsType.php?roomType=" + roomType;
        StringRequest request = new StringRequest(Request.Method.GET, img_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    isReserved = true;
                                    break;
                                }
                            }
                            if (!isReserved) {
                                filteredRooms.add(room);
                            }
                        }


//                        List<Room> filteredRooms = roomList.stream()
//                                .filter(room -> !reservations.stream()
//                                        .filter(reservation -> reservation.getRoomNo() == room.getRoomNo())
//                                        .anyMatch(reservation -> dates.contains(reservation.getDate())))
//                                .collect(Collectors.toList());



//                        Log.d("number of rooms", roomsToShow.size() + "");


                        for (int i = 0; i < filteredRooms.size(); i++) {
                            String img_url = "https://mohammadf.site/Rest/getRoomImg.php";
                            int finalI = i;
                            StringRequest request = new StringRequest(Request.Method.POST, img_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("imgs", response);
                                            try {
                                                Image[] imgArr = gson.fromJson(response, Image[].class);
                                                images = Arrays.asList(imgArr);
                                                filteredRooms.get(finalI).setImages(images);
                                            } catch (Exception e) {

                                            }
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
                                    Log.d("roomNo", Integer.toString(filteredRooms.get(finalI).getRoomNo()));
                                    prems.put("roomNo", Integer.toString(filteredRooms.get(finalI).getRoomNo()));
                                    return prems;
                                }
                            };
                            VolleySingleton.getInstance(Public.context).addToRequestQueue(request);
                        }



                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(filteredRooms.size() == 0){
                                    Toast.makeText(SearchActivity.this, "No rooms available", Toast.LENGTH_SHORT).show();
                                    textView.setText("No rooms available");
                                } else {
                                    Gson gson = new Gson();
                                    editor.putString("roomsToShow", gson.toJson(filteredRooms.toArray()));
                                    editor.putString("targetedDates", gson.toJson(dates.toArray()));
                                    editor.commit();
                                    Intent intent = new Intent(SearchActivity.this, RoomMenuActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }, 3000);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.getMessage());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = DateManager.makeDateString(day, month, year);
                datePicker.setText(date);
            }
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
