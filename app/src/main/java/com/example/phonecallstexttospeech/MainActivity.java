package com.example.phonecallstexttospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.*;

import com.judemanutd.autostarter.AutoStartPermissionHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button talk_button,autostartButton;
    TextToSpeech textToSpeech;
    String phoneNumber = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent speechIntent = new Intent();
        speechIntent.setClass(getApplicationContext(), PhoneCallSpeech.class);
        speechIntent.putExtra("MESSAGE", "Service Started");
        speechIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        getApplicationContext().startService(speechIntent);
        getApplicationContext().startService(speechIntent);

        Intent phoneNumberIntent = getIntent();
        if (phoneNumberIntent.getStringExtra("IncomingNumber") != null) {
            phoneNumber = phoneNumberIntent.getStringExtra("IncomingNumber");
        }

        talk_button = findViewById(R.id.talk_btn);
        autostartButton = findViewById(R.id.autostart_button);

        textToSpeech = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {
                            int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                        }
                    }
                });


        talk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "Phone Call Text To Speech Project";

                int speech = textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        autostartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());
            }
        });

//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_PHONE_STATE)){
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
//            }else{
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
//            }
//        }else{
//            // Nothing
//        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}