package edu.birzeit.zamilihotal.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.Data.Public;
import edu.birzeit.zamilihotal.MainActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
    }

    public void profileClick(MenuItem item) {
    }
    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, BookingActivity.class);
        startActivity(intent);
    }
    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
        startActivity(intent);
    }


    public void deleteAccount(View view) {
        String deleteAccountLink = "https://mohammadf.site/Rest/deleteAccount.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteAccountLink,
                response -> {
                    Objects.requireNonNull(DataBase.auth.getCurrentUser()).delete();
                    DataBase.auth.signOut();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("email", DataBase.auth.getCurrentUser().getEmail());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(stringRequest);
    }
}
