package edu.birzeit.zamilihotal.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import edu.birzeit.zamilihotal.model.User;

public class SignUpController {
    private User user;
    public SignUpController(User user, Context context) {
        this.user = user;
        signUp(context);
    }

    private void signUp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        Gson g = new Gson();
        editor.putString(user.getEmail(), g.toJson(user));
        editor.commit();
    }

}
