package edu.birzeit.zamilihotal.adabter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Reservation;
import edu.birzeit.zamilihotal.model.Review;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationViewer>{


    Context context;
    List<Reservation> reservations;


    public ReservationAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }


    @NonNull
    @Override
    public ReservationViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReservationViewer(LayoutInflater.from(context).inflate(R.layout.reservation_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReservationViewer holder, @SuppressLint("RecyclerView") int position) {
        holder.room_card_title_res.setText("Reservation num# " + reservations.get(position).getReservationNum());
        holder.room_card_number_res.setText("Room num# " + reservations.get(position).getRoomId());
        holder.room_card_date_res.setText("Date: " + reservations.get(position).getDate());
        holder.room_card_numberOfDays.setText("Number of days: " + reservations.get(position).getNumberOfDays());

        //Log.d("test11", reservations.get(position).getIsRated());


        if(reservations.get(position).getIsRated().equals("0")){
            holder.user_room_submit.setClickable(true);
            holder.user_room_submit.setText("Submit");

            holder.user_room_submit.setOnClickListener(v -> {
                if(holder.user_room_rate.getText().toString().isEmpty() || holder.user_room_review.getText().toString().isEmpty()){
                    holder.user_room_rate.setError("Please enter rate");
                    holder.user_room_review.setError("Please enter review");
                } else {

                    Log.d("params", DataBase.auth.getCurrentUser().getEmail());
                    Log.d("params", String.valueOf(reservations.get(position).getRoomId()));
                    Log.d("params", holder.user_room_review.getText().toString());
                    Log.d("params", holder.user_room_rate.getText().toString());



                    String userEmail = DataBase.auth.getCurrentUser().getEmail();
                    int roomNo = reservations.get(position).getRoomId();
                    String review = holder.user_room_review.getText().toString();
                    String rate = holder.user_room_rate.getText().toString();

                    String link = "https://mohammadf.site/Rest/addReview.php?userEmail=" + userEmail +
                                "&roomNo=" + roomNo + "&review=" + review + "&rate=" + rate;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                            response -> Log.e("response", response),
                            error -> Log.e("error", error.getMessage())
                    );


                    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                    link = "https://mohammadf.site/Rest/updateRes.php?ReservationNum=" + reservations.get(position).getReservationNum();
                    stringRequest = new StringRequest(link, response -> {
                        Log.e("response", response);
                    }, error -> {
                        Log.e("error", error.getMessage());
                    });
                    VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

                    holder.user_room_submit.setClickable(false);
                    holder.user_room_submit.setText("Rated");
                    holder.user_room_rate.setText("Rated");
                    holder.user_room_review.setText("Reviewed");

                    holder.user_room_rate.setFocusable(false);
                    holder.user_room_review.setFocusable(false);
                }
            });

        } else {
            holder.user_room_submit.setClickable(false);
            holder.user_room_submit.setText("Rated");
            holder.user_room_rate.setText("Rated");
            holder.user_room_review.setText("Reviewed");
            holder.user_room_rate.setFocusable(false);
            holder.user_room_review.setFocusable(false);
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }
}
