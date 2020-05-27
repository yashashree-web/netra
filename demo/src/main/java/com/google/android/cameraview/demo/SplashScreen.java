package com.google.android.cameraview.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    Button sstar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView textView = findViewById(R.id.HeadName);
        View view = findViewById(R.id.HeadLine);


        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.transistion);
        textView.setAnimation(myanim);
        view.setAnimation(myanim);

        final Intent i = new Intent(this,HomeActvity.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(i);
                    finish();
                }
            }

        };
        timer.start();

    }
}

