package com.example.hiny;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String Tag = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(Tag,"Application Running.....");
                // Splash Screen이 뜨고 나서 실행될 Activity 연결
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
