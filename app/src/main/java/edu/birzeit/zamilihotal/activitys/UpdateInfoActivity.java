package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;
import edu.birzeit.zamilihotal.model.User;

public class UpdateInfoActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button updateButton;
    private boolean changesMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_info);

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        updateButton = findViewById(R.id.updateButton);

        // Retrieve the current user object from Firebase Authentication
        FirebaseUser currentUser = DataBase.auth.getCurrentUser();
        Gson gson = new Gson();

        String url = "https://mohammadf.site/Rest/getUserData.php?user_email="+ currentUser.getEmail();


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("No user found with this email.")){
                    User usr = gson.fromJson(response, User.class);
                    String email = currentUser.getEmail();
                    String firstName = usr.getF_Name();
                    String lastName = usr.getL_Name(); // Replace this with the code to fetch the last name
                    String phoneNumber = usr.getPhone_number(); // Replace this with the code to fetch the phone number

                    // Set the email, first name, last name, and phone number fields with the user's information
                    emailEditText.setText(email);
                    firstNameEditText.setText(firstName);
                    lastNameEditText.setText(lastName);
                    phoneNumberEditText.setText(phoneNumber);

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());

            }

        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);








        // Set a click listener for the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the updated information from the EditText fields
                String updatedFirstName = firstNameEditText.getText().toString().trim();
                String updatedLastName = lastNameEditText.getText().toString().trim();
                String updatedEmail = emailEditText.getText().toString().trim();
                String updatedPhoneNumber = phoneNumberEditText.getText().toString().trim();

                // Create a new User object with the updated information
                User updatedUser = new User(updatedEmail, "", updatedFirstName, updatedLastName, updatedPhoneNumber);

                // Update the user information
                updateUserInformation(updatedUser);
            }
        });

        // Add a listener to the back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChangesAndConfirmExit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        checkChangesAndConfirmExit();
    }

    private void updateUserInformation(User updatedUser) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.updateEmail(updatedUser.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DataBase.database.collection("users").document(firebaseUser.getUid())
                                        .set(updatedUser)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(UpdateInfoActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                    // Return to the profile page
                                                    finish();
                                                } else {
                                                    Toast.makeText(UpdateInfoActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(UpdateInfoActivity.this, "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Are you sure you want to discard the changes?");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UpdateInfoActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void checkChangesAndConfirmExit() {
        String updatedFirstName = firstNameEditText.getText().toString().trim();
        String updatedLastName = lastNameEditText.getText().toString().trim();
        String updatedEmail = emailEditText.getText().toString().trim();
        String updatedPhoneNumber = phoneNumberEditText.getText().toString().trim();

        if (changesMade || !updatedFirstName.isEmpty() || !updatedLastName.isEmpty() || !updatedEmail.isEmpty() || !updatedPhoneNumber.isEmpty()) {
            showConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

}
