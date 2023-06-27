package edu.birzeit.zamilihotal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.activitys.MainPageActivity;
import edu.birzeit.zamilihotal.controllers.SignUpController;
import edu.birzeit.zamilihotal.database.DataBase;
import edu.birzeit.zamilihotal.model.User;

public class MainActivity extends AppCompatActivity {


    private TextView signUpFromSignIn;
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
            Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
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


        EditText pass1 = findViewById(R.id.signUpPass);
        EditText pass2 = findViewById(R.id.repeatpass);
        TextView error = findViewById(R.id.passwordNotEqual);
        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass1.getText().toString().equals(pass2.getText().toString())) {
                    error.setText("Both passwords should be the same");
                } else {

                    SharedPreferences sp = MainActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                    if(sp.getString(email.getText().toString(), null) == null) {
                        SignUpController controller = new SignUpController(new User(email.getText().toString(), pass1.getText().toString()), MainActivity.this);
                        container.removeAllViews();
                        getLayoutInflater().inflate(R.layout.layout_login, container, true);
                        setSignUpFromSignIn();
                    } else {
                        error.setText("email already exits");
                    }
                }
            }
        });

    }



}