package com.android.nosh.src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.nosh.R;
import com.android.nosh.src.login.LoginActivity;

public class MainActivity extends Activity {

    private static int SPLASH_SCREEN = 5000;

    private Animation topAnim, bottomAnim;
    private ImageView logo;
    private TextView title, description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.bullsLogo);
        title = findViewById(R.id.nosh_title);
        description = findViewById(R.id.nosh_description);

        logo.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        description.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);

    }
}
