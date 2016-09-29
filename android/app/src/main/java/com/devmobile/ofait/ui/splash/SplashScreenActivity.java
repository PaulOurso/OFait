package com.devmobile.ofait.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devmobile.ofait.R;
import com.devmobile.ofait.ui.login.LoginActivity;
import com.devmobile.ofait.utils.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Default go on LoginActivity
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                LoginActivity.show(SplashScreenActivity.this);
            }
        }, Constant.SPLASH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
