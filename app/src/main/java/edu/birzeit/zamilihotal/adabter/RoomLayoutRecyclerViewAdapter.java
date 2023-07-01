package edu.birzeit.zamilihotal.adabter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.DownloadImageTask;
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
        holder.room_card_title.setText(rooms.get(position).getRoomType());
        int floor = rooms.get(position).getRoomNo() / 100;
        holder.room_card_floor.setText("Floor: " + floor);
        holder.room_card_number.setText("R" + rooms.get(position).getRoomNo());
        SharedPreferences sp = holder.itemView.getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        holder.room_card_rate.setText(rooms.get(position).getRate() + "/5");
        holder.room_card_price.setText(rooms.get(position).getPrice() + "$ / day");
        String startDate =sp.getString("startDate", "Jul 1 2023");
//        List<String> dates = DateManager.getNextNDates(startDate, sp.getInt("numberOfDays", 1));
        Handler handler = new Handler();

        holder.room_card_date.setText(startDate + " - " + DateManager.getLastDate(startDate, sp.getInt("numberOfDays", 1)));

        Gson gson = new Gson();
        
        holder.room_card_title.setContentDescription(gson.toJson(rooms.get(position)));


        new DownloadImageTask(holder.room_card_img).execute(rooms.get(position).getImages().get(0).getImgURL());

    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
