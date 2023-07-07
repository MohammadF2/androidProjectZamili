package edu.birzeit.zamilihotal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.activitys.SearchActivity;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        container = findViewById(R.id.container);
        getLayoutInflater().inflate(R.layout.layout_login, container, true);

        setSignUpFromSignIn();

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = DataBase.auth.getCurrentUser();
        if(user == null) {
            container.removeAllViews();
            getLayoutInflater().inflate(R.layout.layout_login, container, true);
            setSignUpFromSignIn();
        } else {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @SuppressLint("SetTextI18n")
    private void setSignUpFromSignIn() {
         TextView signUpFromSignIn = findViewById(R.id.signUpFromSignIn);
         EditText email = findViewById(R.id.editTextEmail);
         EditText password = findViewById(R.id.editTextPassword);
         TextView error = findViewById(R.id.errorSignUp);
         Button signInB = findViewById(R.id.cirLoginButton);

         signInB.setOnClickListener(v -> {
             if (email.getText().toString().equals("") || password.getText().toString().equals(""))
                 error.setText("You should enter email and password");
             else {
                 String emailTxt = email.getText().toString();
                 String passwordTxt = password.getText().toString();
                 DataBase.auth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(task -> {
                     if(task.isSuccessful()) {
                        saveUserOnSharedPrefs();
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        MainActivity.this.startActivity(intent);
                     } else {
                         error.setText("Email or password are wrong");
                     }
                 });
             }
         });
         signUpFromSignIn.setOnClickListener(v -> {
             container.removeAllViews();
             getLayoutInflater().inflate(R.layout.layout_register, container, true);
             setSignInFromSignUp();
         });
    }
    private void saveUserOnSharedPrefs() {
        FirebaseUser u = DataBase.auth.getCurrentUser();
        assert u != null;
        String url = "https://mohammadf.site/Rest/getUserData.php?user_email="+ u.getEmail();

        SharedPreferences sp = MainActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            if(!response.equals("No user found with this email.")){
                Log.d("res", response);
                editor.putString("currUser", response);
                editor.apply();
            }
        }, error -> Log.d("Error", error.getMessage()));

        runOnUiThread(() -> VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(request));
    }
    @SuppressLint("SetTextI18n")
    private void setSignInFromSignUp() {
        TextView signInFromSignUp = findViewById(R.id.signInFromSignUp);
        signInFromSignUp.setOnClickListener(v -> {
            container.removeAllViews();
            getLayoutInflater().inflate(R.layout.layout_login, container, true);
            setSignUpFromSignIn();
        });

        Button signUpB = findViewById(R.id.signUpB);
        EditText email = findViewById(R.id.signUpEmail);

        EditText F_name = findViewById(R.id.signUpFname);
        EditText L_name = findViewById(R.id.signUpLname);
        EditText phone = findViewById(R.id.signUpPhoneNumber);
        TextView error = findViewById(R.id.errorSignUp);
        EditText pass1 = findViewById(R.id.signUpPass);
        EditText pass2 = findViewById(R.id.repeatpass);


        signUpB.setOnClickListener(v -> {
            if(F_name.getText().toString().equals("") || L_name.getText().toString().equals("") || phone.getText().toString().equals("") ||
                    email.getText().toString().equals("") || pass1.getText().toString().equals("") || pass2.getText().toString().equals("")){
                error.setText("all fields are required");
                return;
            }
            if(!pass1.getText().toString().equals(pass2.getText().toString())) {
                error.setText("Both passwords should be the same");
            } else {
                String emailTXT = email.getText().toString();
                String password = pass2.getText().toString();

                DataBase.auth.createUserWithEmailAndPassword(emailTXT, password).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        error.setText(Objects.requireNonNull(task.getException()).getMessage());
                    } else {
                        String url = "https://mohammadf.site/Rest/InsertUser.php?user_email="+email.getText().toString()+"&user_Fname="+F_name.getText().toString()
                                +"&user_Lname="+L_name.getText().toString()+"&user_phone="+phone.getText().toString();
                        System.out.println(url);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                            container.removeAllViews();
                            getLayoutInflater().inflate(R.layout.layout_login, container, true);
                            setSignUpFromSignIn();
                        }, error1 -> error.setText(error1.getMessage()));
                        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                DataBase.auth.signOut();
            }
        });

    }

}