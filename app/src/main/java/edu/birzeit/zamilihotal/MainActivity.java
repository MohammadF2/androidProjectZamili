package edu.birzeit.zamilihotal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.activitys.MainPageActivity;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.activitys.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private static TextView signUpFromSignIn;
    private TextView signInFromSignUp;

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

    private void setSignUpFromSignIn() {
        signUpFromSignIn = findViewById(R.id.signUpFromSignIn);

         EditText email = findViewById(R.id.editTextEmail);
         EditText password = findViewById(R.id.editTextPassword);
         TextView error = findViewById(R.id.errorSignUp);
         Button signInB = findViewById(R.id.cirLoginButton);

         signInB.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (email.getText().toString().equals("") || password.getText().toString().equals(""))
                     error.setText("You should enter email and password");
                 else {
                     String emailTxt = email.getText().toString();
                     String passwordTxt = password.getText().toString();
                     DataBase.auth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()) {
                                saveUserOnSharedPrefs();
                                Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                                MainActivity.this.startActivity(intent);
                             } else {
                                 error.setText("Email or password are wrong");
                             }
                         }
                     });
                 }
             }
         });

        signUpFromSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                getLayoutInflater().inflate(R.layout.layout_register, container, true);
                setSignInFromSignUp();
            }
        });
    }


    private void saveUserOnSharedPrefs() {
        FirebaseUser u = DataBase.auth.getCurrentUser();
        String url = "https://mohammadf.site/Rest/getUserData.php?user_email="+ u.getEmail();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        SharedPreferences sp = MainActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("No user found with this email.")){
                    Log.d("res", response);
                    editor.putString("currUser", response);
                    editor.commit();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queue.add(request);
            }
        });
    }

    private void setSignInFromSignUp() {
        signInFromSignUp = findViewById(R.id.signInFromSignUp);
        signInFromSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                getLayoutInflater().inflate(R.layout.layout_login, container, true);
                setSignUpFromSignIn();
            }
        });

        Button signUpB = findViewById(R.id.signUpB);
        EditText email = findViewById(R.id.signUpEmail);

        EditText F_name = findViewById(R.id.signUpFname);
        EditText L_name = findViewById(R.id.signUpLname);
        EditText phone = findViewById(R.id.signUpPhoneNumber);
        TextView error = findViewById(R.id.errorSignUp);
        EditText pass1 = findViewById(R.id.signUpPass);
        EditText pass2 = findViewById(R.id.repeatpass);


        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    DataBase.auth.createUserWithEmailAndPassword(emailTXT, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                error.setText(task.getException().getMessage());
                            } else {
                                String url = "https://mohammadf.site/Rest/InsertUser.php?user_email="+email.getText().toString()+"&user_Fname="+F_name.getText().toString()
                                        +"&user_Lname="+L_name.getText().toString()+"&user_phone="+phone.getText().toString();
                                System.out.println(url);
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        container.removeAllViews();
                                        getLayoutInflater().inflate(R.layout.layout_login, container, true);
                                        setSignUpFromSignIn();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error1) {
                                        error.setText(error1.getMessage());
                                    }
                                });
                                queue.add(stringRequest);
                            }
                        }
                    });

                    DataBase.auth.signOut();
                }
            }
        });

    }

}