package com.glowingsoft.photographist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cunoraz.gifview.library.GifView;
import com.glowingsoft.photographist.R;
import com.whygraphics.gifview.gif.GIFView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GifView gifView1 = (GifView) findViewById(R.id.gif1);
        gifView1.setVisibility(View.VISIBLE);

        gifView1.setGifResource(R.drawable.gif1);
        gifView1.play();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
