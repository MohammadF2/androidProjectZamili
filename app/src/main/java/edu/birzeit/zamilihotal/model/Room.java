package edu.birzeit.zamilihotal.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.Data.Public;
import edu.birzeit.zamilihotal.MainActivity;

public class Room implements Comparable<Room>{
    private Hotel hotel;
    private int roomNo;
    private double rate;
    private double price;
    private String roomType;
    private List<Image> images = new ArrayList<>();
    private String description;

//    public Room(Hotel hotel, int roomNo, String roomType, List<Image> images, double price, String description, Context context) {
//        this.hotel = hotel;
//        this.roomNo = roomNo;
//        this.roomType = roomType;
//        this.images = images;
//        rate = 5;
//        this.price = price;
//        this.description = description;
//        addToDataBase(context);
//    }

    public Room(int roomNo,String roomType ,double rate, double price, String description) {
        this.roomNo = roomNo;
        this.rate = rate;
        this.price = price;
        this.roomType = roomType;
        this.description = description;
        getImgFromDatabase();
    }

    private void getImgFromDatabase() {

        Gson gson = new Gson();
        String img_url = "https://mohammadf.site/Rest/getRoomImg.php";
        RequestQueue queue = Volley.newRequestQueue(Public.context);
        StringRequest request = new StringRequest(Request.Method.POST, img_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("imgs", response);
                        Image[] imgArr = gson.fromJson(response, Image[].class);
                        images = Arrays.asList(imgArr);
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
                return prems;
            }
        };
        queue.add(request);
    }

    private void addToDataBase(Context context) {
        String url = "https://mohammadf.site/Rest/addRoom.php";

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("One_Of_your_data_is_null")){
                            for (int i = 0; i < images.size(); i++) {
                                addImg(images.get(i).getImgURL(), context);
                            }
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("roomNo", Integer.toString(roomNo));
                params.put("price", Double.toString(price));
                params.put("roomType", roomType);
                params.put("description", description);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void addImg(String imgURL, Context context) {
        String img_url = "https://mohammadf.site/Rest/addImage.php";
        RequestQueue queue = Volley.newRequestQueue(context);
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
                    Map<String, String> params = new HashMap<>();
                    params.put("roomNo", Integer.toString(roomNo));
                    params.put("imageUrl", imgURL);
                    return params;
                }
            };
            queue.add(request);
        }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public int compareTo(Room o) {
        return Integer.compare(roomNo, o.getRoomNo());
    }
}
