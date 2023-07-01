package edu.birzeit.zamilihotal.adabter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.birzeit.androidprojectzamili.R;

public class RoomCardViewer extends RecyclerView.ViewHolder {


    TextView room_card_title;
    TextView room_card_floor;
    TextView room_card_number;
    TextView room_card_date;
    TextView room_card_rate;
    TextView room_card_price;
    ImageView room_card_img;
    LinearLayout room_card_ground;


    public RoomCardViewer(@NonNull View itemView) {
        super(itemView);
        room_card_ground = itemView.findViewById(R.id.room_card_ground);
         room_card_title = itemView.findViewById(R.id.room_card_title);
         room_card_floor = itemView.findViewById(R.id.room_card_floor);
         room_card_number = itemView.findViewById(R.id.room_card_number);
         room_card_date = itemView.findViewById(R.id.room_card_date);
         room_card_rate = itemView.findViewById(R.id.room_card_rate);
         room_card_price = itemView.findViewById(R.id.room_card_price);
         room_card_img = itemView.findViewById(R.id.room_card_img);
    }
}
