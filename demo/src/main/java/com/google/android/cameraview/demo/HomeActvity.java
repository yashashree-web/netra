package com.google.android.cameraview.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActvity extends AppCompatActivity {
    TextToSpeech t1;
    Button myNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actvity);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        ImageView image_speak=(ImageView)findViewById(R.id.image_speak);
        image_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak("select the option face recognise ,object recognise and text recognise",TextToSpeech.QUEUE_FLUSH,null);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        AddAutomation();
                    }
                }, 4000);

            }
        });



    }
    private void AddAutomation() {
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra
                (RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "face recognise");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "face recognize");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "object recognise");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "object recognize");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "text recognise");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "text recognize");
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "face recognition");

        startActivityForResult(voice, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (arrayList.get(0).toString().equals("face recognise") || arrayList.get(0).toString().equals
                    ("face recognize") || arrayList.get(0).toString().equals("face recognition")) {
                startActivity(new Intent(HomeActvity.this,MainActivity.class));

            }
            else if (arrayList.get(0).toString().equals("object recognise") || arrayList.get(0).toString().equals
                    ("object recognize") || arrayList.get(0).toString().equals("object recognition")){
                startActivity(new Intent(HomeActvity.this,Camera2Activity.class));

            }
            else if (arrayList.get(0).toString().equals("text recognise") || arrayList.get(0).toString().equals
                    ("text recognize") || arrayList.get(0).toString().equals("text recognition")){
                startActivity(new Intent(HomeActvity.this,TextActivity.class));
            }
            else{
                AddAutomation();
            }

        }
    }
}
