package edu.birzeit.zamilihotal.activitys;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.model.User;

public class UpdateInfoActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText;
    private Button updateButton;
    private User currentUser;
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

        // Retrieve the current user object from shared preferences
        currentUser = getCurrentUserFromSharedPrefs();

        // Populate the EditText fields with the current user's information
        if (currentUser != null) {
            firstNameEditText.setText(currentUser.getF_Name());
            lastNameEditText.setText(currentUser.getL_Name());
            emailEditText.setText(currentUser.getEmail());
            phoneNumberEditText.setText(currentUser.getPhone_number());
        }

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
                User updatedUser = new User(updatedEmail, currentUser.getPassword(), updatedFirstName, updatedLastName, updatedPhoneNumber);

                // Update the user information
                updateUserInformation(updatedUser);
            }
        });

        // Add a listener to the back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (changesMade) {
            showConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private User getCurrentUserFromSharedPrefs() {
        SharedPreferences sp = getSharedPreferences("main", MODE_PRIVATE);
        String userData = sp.getString("currUser", null);
        if (userData != null) {
            try {
                JSONObject userObj = new JSONObject(userData);
                String email = userObj.getString("email");
                String firstName = userObj.getString("f_Name");
                String lastName = userObj.getString("l_Name");
                String phoneNumber = userObj.getString("phone_number");

                // Retrieve the password from Firebase or from the user profile data
                String password = ""; // Replace this with the code to fetch the password

                // Set the email field with the user's email
                emailEditText.setText(email);
                firstNameEditText.setText(firstName);
                lastNameEditText.setText(lastName);
                phoneNumberEditText.setText(phoneNumber);

                return new User(email, password, firstName, lastName, phoneNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
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
                                                    onBackPressed();
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
}
