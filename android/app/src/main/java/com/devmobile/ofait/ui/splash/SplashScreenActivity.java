package com.devmobile.ofait.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.ui.login.LoginActivity;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Constant;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Default go on LoginActivity
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
            if(account.fb_id != null)
                FBConnect(account);
            else if(account.google_id!= null)
                GoogleConnect(account);
        }
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

    private void GoogleConnect(Account account) {
        checkAccount(account);
    }

    private void FBConnect(final Account account){
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                AccessToken.refreshCurrentAccessTokenAsync(new AccessToken.AccessTokenRefreshCallback() {
                    @Override
                    public void OnTokenRefreshed(AccessToken accessToken) {
                        if(accessToken != null){
                            Log.d("SplashScreen", "Logged in");
                            account.fb_id = accessToken.getUserId();
                            checkAccount(account);
                        }
                        else {
                            Log.d("SplashScreen", "Not logged in");
                        }
                    }

                    @Override
                    public void OnTokenRefreshFailed(FacebookException exception) {
                        Log.d("SplashScreen", "Token refresh failed");
                    }
                });
            }
        });
    }

    public void checkAccount(Account acc) {
        APIHelper.getAccountFromLogin(SplashScreenActivity.this, true, acc, new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                if (answer.status < 300) {
                    Account account = answer.data;
                    if (account.pseudo != null) {
                        stopTimer();
                        Preference.setAccount(SplashScreenActivity.this, answer.data);
                        MainActivity.show(SplashScreenActivity.this);
                    }
                }
                else
                    Preference.setAccount(SplashScreenActivity.this, null);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
