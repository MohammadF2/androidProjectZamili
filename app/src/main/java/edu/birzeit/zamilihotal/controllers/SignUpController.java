package edu.birzeit.zamilihotal.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import java.util.Objects;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.database.DataBase;
import edu.birzeit.zamilihotal.model.User;

public class SignUpController {
    private User user;
    private TextView error;
    private boolean isSuccess;

    public SignUpController(User user, Context context, TextView error) {
        this.user = user;
        this.error = error;
        signUp(context);
    }

    private void signUp(Context context) {
        String email = user.getEmail();
        String password = user.getPassword();
        isSuccess = true;
        DataBase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    isSuccess = false;
                    error.setText(task.getException().getMessage());
                } else {
                    Map<String, String> userAccount = new HashMap<>();
                    userAccount.put("F_Name", user.getF_Name());
                    userAccount.put("L_Name", user.getL_Name());
                    userAccount.put("phone", user.getPhone_number());
                    DataBase.database.collection("users").document(user.getEmail()).set(userAccount).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Objects.requireNonNull(DataBase.auth.getCurrentUser()).delete();
                            error.setText(e.getMessage());
                        }
                    });
                }
            }
        });
        DataBase.auth.signOut();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
