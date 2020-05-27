package com.google.android.cameraview.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.PointerIconCompat;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.PointerIconCompat;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.gms.vision.text.TextRecognizer.Builder;
import java.io.IOException;
import java.util.Locale;

public class TextActivity extends AppCompatActivity {
    final int RequesCameraPermission = PointerIconCompat.TYPE_CONTEXT_MENU;
    CameraSource cameraSource;
    SurfaceView cameraView;
    /* access modifiers changed from: private */
    public Button mButtonSpeak;
    /* access modifiers changed from: private */
    public TextToSpeech mtts;
    private SeekBar seek1;
    private SeekBar seek2;
    TextView textView;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001 && grantResults[0] == 0 && ActivityCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
            try {
                this.cameraSource.start(this.cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        this.cameraView = (SurfaceView) findViewById(R.id.surface_iew);
        this.textView = (TextView) findViewById(R.id.text_view);
        TextRecognizer textRecognizer = new Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.e("TextActivity", "Detector depedencies not availabale");
            return;
        }
        this.cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer).setFacing(0).setRequestedPreviewSize(1280, 1024).setRequestedFps(2.0f).setAutoFocusEnabled(true).build();
        this.cameraView.getHolder().addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                String str = "android.permission.CAMERA";
                try {
                    if (ActivityCompat.checkSelfPermission(TextActivity.this.getApplicationContext(), str) != 0) {
                        ActivityCompat.requestPermissions(TextActivity.this, new String[]{str}, PointerIconCompat.TYPE_CONTEXT_MENU);
                        return;
                    }
                    TextActivity.this.cameraSource.start(TextActivity.this.cameraView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                TextActivity.this.cameraSource.stop();
            }
        });
        textRecognizer.setProcessor(new Processor<TextBlock>() {
            public void release() {
            }

            public void receiveDetections(Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    TextActivity.this.textView.post(new Runnable() {
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < items.size(); i++) {
                                stringBuilder.append(((TextBlock) items.valueAt(i)).getValue());
                                stringBuilder.append("\n");
                            }
                            TextActivity.this.textView.setText(stringBuilder.toString());
                            TextActivity.this.speak();
                        }
                    });
                }
            }
        });
        this.mButtonSpeak = (Button) findViewById(R.id.buttonSpeak);
        this.mtts = new TextToSpeech(this, new OnInitListener() {
            public void onInit(int status) {
                String str = "TTS";
                if (status == 0) {
                    int result = TextActivity.this.mtts.setLanguage(Locale.ENGLISH);
                    if (result == -1 || result == -2) {
                        Log.e(str, "Language not supported");
                    } else {
                        TextActivity.this.mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e(str, "Initialization not done");
                }
            }
        });
        this.seek1 = (SeekBar) findViewById(R.id.seek1);
        this.seek2 = (SeekBar) findViewById(R.id.seek2);
        this.mButtonSpeak.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               TextActivity.this.speak();
            }
        });
    }

    public void speak() {
        String text = this.textView.getText().toString();
        float pitch = ((float) this.seek1.getProgress()) / 50.0f;
        if (((double) pitch) < 0.1d) {
            pitch = 0.1f;
        }
        float sound = ((float) this.seek2.getProgress()) / 50.0f;
        if (((double) sound) < 0.1d) {
            sound = 0.1f;
        }
        this.mtts.setPitch(pitch);
        this.mtts.setSpeechRate(sound);
        this.mtts.speak(text, 0, null);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        TextToSpeech textToSpeech = this.mtts;
        if (textToSpeech != null) {
            textToSpeech.stop();
            this.mtts.shutdown();
        }
        super.onDestroy();
    }

}
