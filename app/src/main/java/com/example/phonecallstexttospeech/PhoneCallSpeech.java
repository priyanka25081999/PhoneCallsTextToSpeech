package com.example.phonecallstexttospeech;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class PhoneCallSpeech extends Service{
    private String spokenText="Sanket";
    TextToSpeech textToSpeech;
    @Override
    public void onCreate() {
        textToSpeech = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS){
                            int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                        }
                    }
                });
        textToSpeech.speak("Started",TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spokenText = intent.getStringExtra("MESSAGE");
        System.out.println("Service : " + spokenText);
        int speech1 = textToSpeech.speak(spokenText,TextToSpeech.QUEUE_ADD,null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}