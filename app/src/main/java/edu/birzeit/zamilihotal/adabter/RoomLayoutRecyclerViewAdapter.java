package edu.birzeit.zamilihotal.adabter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.activitys.RoomActivity;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.DownloadImageTask;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Room;

@SuppressWarnings("ALL")
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





    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomCardViewer holder, @SuppressLint("RecyclerView") int position) {

        SharedPreferences sp = holder.itemView.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);

        String startDate =sp.getString("startDate", "Jul 1 2023");
        holder.room_card_date.setText(startDate + " - " + DateManager.getLastDate(startDate, sp.getInt("numberOfDays", 1)));
        String[] rates = new String[rooms.size()];

        holder.room_card_title.setText(rooms.get(position).getRoomType());
        int floor = rooms.get(position).getRoomNo() / 100;
        holder.room_card_floor.setText("Floor: " + floor);
        holder.room_card_number.setText("R" + rooms.get(position).getRoomNo());

        getRate(rooms.get(position).getRoomNo(), position, rates);
        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (rates[position].length() > 3) {
                holder.room_card_rate.setText(rates[position].substring(0, 3) + "/5");
            } else {
                holder.room_card_rate.setText(rates[position] + "/5");
            }
        }, 1000);
        holder.room_card_price.setText(rooms.get(position).getPrice() + "$ / day");


        holder.room_card_ground.setOnClickListener(v -> {
            SharedPreferences sp1 = holder.itemView.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            Gson g = new Gson();
            String target = g.toJson(rooms.get(position));
            editor.putString("target", target);
            editor.apply();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(holder.itemView.getContext(), RoomActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }, 100);
        });

        new DownloadImageTask(holder.room_card_img).execute(rooms.get(position).getImages().get(0).getImgURL());

    }


    private void getRate(int roomNo, int position, String[] rates) {
        String link = "https://mohammadf.site/Rest/getAvgRate.php?roomNo=" + roomNo;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, response -> {
            Pattern pattern = Pattern.compile("\"([0-9]+(\\.[0-9]+)?)\"");
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                rates[position] = matcher.group(1);
            } else {
                rates[position] = "5";
            }
            Log.d("TAG", "getRate: " + rates[position]);
        }, error -> Log.d("TAG", "getRate: " + error.getMessage()));

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, rate::set, error -> Log.d("TAG", "getRate: " + error.getMessage()));
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
