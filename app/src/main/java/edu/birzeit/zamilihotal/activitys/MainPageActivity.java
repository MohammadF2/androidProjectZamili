package edu.birzeit.zamilihotal.activitys;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.database.DataBase;


public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        TextView t = findViewById(R.id.textView);

        FirebaseUser user = DataBase.auth.getCurrentUser();

        t.setText(user.getEmail());

    }
}
