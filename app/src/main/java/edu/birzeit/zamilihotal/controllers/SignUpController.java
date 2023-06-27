package edu.birzeit.zamilihotal.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.activitys.MainPageActivity;
import edu.birzeit.zamilihotal.database.DataBase;
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

        String email = user.getEmail();
        String password = user.getPassword();

        DataBase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Login", "Pass " + email + " " + password);
                } else {

                    Log.d("Login", "Fail " + email + " " + password + task.getException());
                }
            }
        });

        Gson g = new Gson();
        editor.putString(user.getEmail(), g.toJson(user));
        editor.commit();
    }

}
