package edu.birzeit.zamilihotal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.activitys.MainPageActivity;
import edu.birzeit.zamilihotal.controllers.SignUpController;
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

        System.out.println();

//        container.removeAllViews();
//        getLayoutInflater().inflate(R.layout.layout_register, container, true);

        setSignUpFromSignIn();

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
                     SharedPreferences sp = MainActivity.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                     Gson gson = new Gson();
                     User user = gson.fromJson(sp.getString(email.getText().toString(), null), User.class);
                     if (user == null)
                         error.setText("Email is wrong");
                     else {
                         if(!password.getText().toString().equals(user.getPassword()))
                             error.setText("password is wrong");
                         else {
                             SharedPreferences.Editor editor = sp.edit();
                             editor.putString("currUser", gson.toJson(user));
                             editor.commit();
                             Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                             startActivity(intent);
                         }
                     }
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
        EditText name = findViewById(R.id.signUpName);
        EditText email = findViewById(R.id.signUpEmail);
        EditText mobile = findViewById(R.id.signUpMobile);

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
                        SignUpController controller = new SignUpController(new User(name.getText().toString(), mobile.getText().toString(), email.getText().toString(), pass1.getText().toString()), MainActivity.this);
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