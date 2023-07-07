package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.Objects;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
    }

    public void profileClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
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
                params.put("email", Objects.requireNonNull(DataBase.auth.getCurrentUser()).getEmail());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void logout(View view) {
        SharedPreferences sp = ProfileActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        DataBase.auth.signOut();
        editor.remove("currUser");
        editor.apply();
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(i);
    }
}
