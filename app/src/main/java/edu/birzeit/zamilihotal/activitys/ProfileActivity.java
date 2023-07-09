package edu.birzeit.zamilihotal.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.Data.DataBase;
import edu.birzeit.zamilihotal.MainActivity;
import edu.birzeit.zamilihotal.controllers.VolleySingleton;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);


        ImageView pinIcon = findViewById(R.id.pinIcon);
        ImageView profilePicture = findViewById(R.id.profilePicture);

        pinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Load the user's profile picture if available
        SharedPreferences sp = getSharedPreferences("main", Context.MODE_PRIVATE);
        String profilePictureUri = sp.getString("profilePictureUri", null);
        if (profilePictureUri != null) {
            profilePicture.setImageURI(Uri.parse(profilePictureUri));
        }
    }


    private void openImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView profilePicture = findViewById(R.id.profilePicture);
                profilePicture.setImageBitmap(bitmap);

                // Save the profile picture URI to SharedPreferences
                SharedPreferences sp = getSharedPreferences("main", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("profilePictureUri", imageUri.toString());
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to set profile picture", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void profileClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void BookingClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, BookingActivity.class);
        startActivity(intent);
    }

    public void SearchClick(MenuItem item) {
        Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmDeleteAccount();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void confirmDeleteAccount() {
        String deleteAccountLink = "https://mohammadf.site/Rest/deleteAccount.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteAccountLink,
                response -> {
                    Objects.requireNonNull(DataBase.auth.getCurrentUser()).delete().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DataBase.auth.signOut();
                                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                },
                error -> {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("email", Objects.requireNonNull(DataBase.auth.getCurrentUser()).getEmail());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity(); // Finish all activities in the stack
    }
    public void editPersonalInfo(View view) {
        Intent intent = new Intent(ProfileActivity.this, UpdateInfoActivity.class);
        startActivity(intent);
    }

    public void changePassword(View view) {
        Intent intent = new Intent(ProfileActivity.this, UpdatePasswordActivity.class);
        startActivity(intent);
    }




}