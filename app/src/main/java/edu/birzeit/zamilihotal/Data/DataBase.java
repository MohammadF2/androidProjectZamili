package edu.birzeit.zamilihotal.Data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBase {
    public static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();
}
