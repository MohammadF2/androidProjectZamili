package edu.birzeit.zamilihotal.activitys;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.birzeit.androidprojectzamili.R;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText rewritePasswordEditText;
    private Button updateButton;
    private Button backpasswordButton;

    private boolean changesSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);

        currentPasswordEditText = findViewById(R.id.CurrentPasswordEditText);
        newPasswordEditText = findViewById(R.id.NewPasswordEditText);
        rewritePasswordEditText = findViewById(R.id.rewritePasswordEditText);
        updateButton = findViewById(R.id.updateButton);
        backpasswordButton = findViewById(R.id.backpasswordButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        backpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String rewritePassword = rewritePasswordEditText.getText().toString().trim();

        if (currentPassword.isEmpty() && newPassword.isEmpty() && rewritePassword.isEmpty()) {
            // No changes made, navigate back without confirmation dialog
            super.onBackPressed();
        } else {
            // Show confirmation dialog
            showDiscardChangesDialog();
        }
    }

    private void showDiscardChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard Changes");
        builder.setMessage("Are you sure you want to discard the changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Changes not saved, navigate back
                changesSaved = false;
                UpdatePasswordActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void showConfirmationDialog() {
        // Implement your logic to save changes here
        // For simplicity, let's assume changes are successfully saved
        changesSaved = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Changes Saved");
        builder.setMessage("Your changes have been saved.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void finish() {
        // Set the result based on whether changes were saved or not
        Intent resultIntent = new Intent();
        resultIntent.putExtra("changesSaved", changesSaved);
        setResult(Activity.RESULT_OK, resultIntent);
        super.finish();
    }
}
