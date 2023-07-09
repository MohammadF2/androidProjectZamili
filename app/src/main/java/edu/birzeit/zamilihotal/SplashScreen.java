package edu.birzeit.zamilihotal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.birzeit.androidprojectzamili.R;

public class SplashScreen extends AppCompatActivity {


    private ImageView img;
    private TextView txt;

    private Animation top, bottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        top = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.splash_anim_bottom);

        img = findViewById(R.id.imgAni);
        txt = findViewById(R.id.txtAni);

        txt.setAnimation(bottom);
        img.setAnimation(top);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);

    }
}
