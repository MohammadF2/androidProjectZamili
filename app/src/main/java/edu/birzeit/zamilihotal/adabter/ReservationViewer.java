package edu.birzeit.zamilihotal.adabter;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.controllers.InputFilterMinMax;

public class ReservationViewer extends RecyclerView.ViewHolder{

    TextView room_card_title_res, room_card_number_res, room_card_numberOfDays, room_card_date_res;

    EditText user_room_rate, user_room_review;

    Button user_room_submit;

    public ReservationViewer(@NonNull View itemView) {
        super(itemView);

        room_card_title_res = itemView.findViewById(R.id.room_card_title_res);
        room_card_number_res = itemView.findViewById(R.id.room_card_number_res);
        room_card_numberOfDays = itemView.findViewById(R.id.room_card_numberOfDays);
        room_card_date_res = itemView.findViewById(R.id.room_card_date_res);
        user_room_rate = itemView.findViewById(R.id.user_room_rate);
        user_room_review = itemView.findViewById(R.id.user_room_review);

        user_room_rate.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5")});

        user_room_submit = itemView.findViewById(R.id.user_room_submit);


    }
}
