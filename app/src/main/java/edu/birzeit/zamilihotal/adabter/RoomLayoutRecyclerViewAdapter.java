package edu.birzeit.zamilihotal.adabter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import edu.birzeit.androidprojectzamili.R;
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
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
