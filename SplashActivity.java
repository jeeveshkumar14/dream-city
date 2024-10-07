package com.example.splash_screen;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.Window;

import android.view.WindowManager;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashactivity);

        Thread splashTread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    if (Main_prefence.getInstance().user_login.equals("Login")) {
                        startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginSelectionActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        splashTread.start();
    }
}