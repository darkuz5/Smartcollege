package com.firewallsol.smartcollege;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        new Timer().schedule(new TimerTask() {
            public void run() {
                SplashScreen.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        }, 2000);


    }

}
