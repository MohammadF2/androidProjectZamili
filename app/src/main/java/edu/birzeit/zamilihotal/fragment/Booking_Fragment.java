package edu.birzeit.zamilihotal.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.adabter.ReservationAdapter;
import edu.birzeit.zamilihotal.adabter.RoomLayoutRecyclerViewAdapter;
import edu.birzeit.zamilihotal.controllers.DateManager;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.Reservation;

public class Booking_Fragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static Booking_Fragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Booking_Fragment fragment = new Booking_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curr, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_curr);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getReservations();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            recyclerView.setAdapter(new ReservationAdapter(getContext(), reservations));
        }, 1550);
        return view;
    }
    List<Reservation> reservations = new ArrayList<>();
    String res = "";

    private void getReservations() {

        String link = "https://mohammadf.site/Rest/getUniqRes.php?userEmail="+ Objects.requireNonNull(DataBase.auth.getCurrentUser()).getEmail();

        Gson gson = new Gson();

        StringRequest stringRequest = new StringRequest(link, response -> {
            Reservation[] reservationsArr = gson.fromJson(response, Reservation[].class);
            this.res = response;
            this.reservations = new ArrayList<>(Arrays.asList(reservationsArr));
            Log.d("reservations", response);
        }, error -> {
            Log.d("error", error.getMessage());
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        Log.d("mPage", mPage + " ");

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            List<String> isRated = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(res);
//                Log.d("jsonArray", jsonArray.length() + " ");
//                Log.d("Reservation", reservations.size() + " ");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    isRated.add(jsonObject.getString("IsRated"));
                    Log.d("jsonObject", jsonObject.getString("IsRated"));
                }
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            }

            Iterator<Reservation> iterator = reservations.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Log.d("index", isRated.get(index));
                Reservation reservation = iterator.next();
                reservation.setIsRated(isRated.get(index));
                index++;
                String last = DateManager.getLastDate(reservation.getDate(), reservation.getNumberOfDays());
                if (mPage == 1 && DateManager.isDateInPast(last)) {
                    iterator.remove();
                } else if (mPage != 1 && !DateManager.isDateInPast(last)) {
                    iterator.remove();
                }
            }
        }, 1500);
    }



}
