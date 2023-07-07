package edu.birzeit.zamilihotal.adabter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.activitys.RoomActivity;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.DownloadImageTask;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Room;

public class RoomLayoutRecyclerViewAdapter extends RecyclerView.Adapter<RoomCardViewer> {

    Context context;
    List<Room> rooms;

    public RoomLayoutRecyclerViewAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomCardViewer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomCardViewer(LayoutInflater.from(context).inflate(R.layout.room_card_layout, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull RoomCardViewer holder, int position) {

        SharedPreferences sp = holder.itemView.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);

        String startDate =sp.getString("startDate", "Jul 1 2023");
        holder.room_card_date.setText(startDate + " - " + DateManager.getLastDate(startDate, sp.getInt("numberOfDays", 1)));

        holder.room_card_title.setText(rooms.get(position).getRoomType());
        int floor = rooms.get(position).getRoomNo() / 100;
        holder.room_card_floor.setText("Floor: " + floor);
        holder.room_card_number.setText("R" + rooms.get(position).getRoomNo());
        holder.room_card_rate.setText(getRate(rooms.get(position).getRoomNo()) + "/5");
        holder.room_card_price.setText(rooms.get(position).getPrice() + "$ / day");


        holder.room_card_ground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = holder.itemView.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                Gson g = new Gson();
                String target = g.toJson(rooms.get(position));
                editor.putString("target", target);
                editor.commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(holder.itemView.getContext(), RoomActivity.class);
                        holder.itemView.getContext().startActivity(intent);
                    }
                }, 100);
            }
        });

        new DownloadImageTask(holder.room_card_img).execute(rooms.get(position).getImages().get(0).getImgURL());

    }

    private String getRate(int roomNo) {
        String link = "https://mohammadf.site/Rest/getAvgRate.php?roomNo=" + roomNo;
        AtomicReference<String> rate = new AtomicReference<>("");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, rate::set, error -> {
            Log.d("TAG", "getRate: " + error.getMessage());
        });
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        return rate.get();
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
