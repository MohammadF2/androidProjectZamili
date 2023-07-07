package edu.birzeit.zamilihotal.Data;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBase {
    @SuppressLint("StaticFieldLeak")
    public static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
}
