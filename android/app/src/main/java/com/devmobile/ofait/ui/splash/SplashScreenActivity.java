package com.devmobile.ofait.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.ui.categories.CategoriesActivity;
import com.devmobile.ofait.ui.login.LoginActivity;
import com.devmobile.ofait.utils.Constant;
import com.devmobile.ofait.utils.Preference;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        // Default go on LoginActivity
    }

    @Override
    protected void onResume() {
        super.onResume();


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                LoginActivity.show(SplashScreenActivity.this);
            }
        }, Constant.SPLASH_TIME);

        // Check if has acccount & connected
        Account account = Preference.getAccount(SplashScreenActivity.this);

        if (account != null) {

            if(account.fb_id != null){
                FBConnect(account);
            }
            else if(account.google_id!= null){
                GoogleConnect(account);
            }

        }
    }

    private void GoogleConnect(Account account) {
        CategoriesActivity.show(this);
    }

    private void FBConnect(final Account account){
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {

                AccessToken.refreshCurrentAccessTokenAsync(new AccessToken.AccessTokenRefreshCallback() {
                    @Override
                    public void OnTokenRefreshed(AccessToken accessToken) {

                        if(accessToken != null){

                            System.out.println("Logged in");

                            stopTimer();
                            account.fb_id = accessToken.getUserId();

                            //todo: voir si un compte comportant ce fb_id existe deja

                            //si un compte exist deja, on ne va pas au login
                            Preference.setAccount(SplashScreenActivity.this,account);
                            CategoriesActivity.show(SplashScreenActivity.this);
                        }
                        else {
                            System.out.println("Not logged in");
                        }
                    }

                    @Override
                    public void OnTokenRefreshFailed(FacebookException exception) {
                        System.out.println("Token refresh failed");
                    }
                });


            }
        });
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
