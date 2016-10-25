package com.devmobile.ofait.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.Constant;
import com.devmobile.ofait.utils.Preference;
import com.devmobile.ofait.utils.requests.APIHelper;
import com.devmobile.ofait.utils.requests.TaskComplete;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFacebook();
        setContentView(R.layout.activity_login);
        initGoogle();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void next() {
        MainActivity.show(LoginActivity.this);
        finish();
    }

    public void initGoogle() {
        SignInButton button = (SignInButton) findViewById(R.id.google_sign_in_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "clicked");
                    googleSignIn();
                }
            });
        }
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, R.string.login_toast_error_identification, Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    
    public void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        onResponseFacebook(loginResult);
                    }

                    @Override
                    public void onCancel() { }

                    @Override
                    public void onError(FacebookException exception) { }
                });
    }

    public void googleSignIn() {
        Log.d(TAG, "signIn clicked");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constant.RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constant.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            onResponseGoogle(result);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onResponseGoogle(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            account = new Account();
            GoogleSignInAccount acct = result.getSignInAccount();
            account.google_id = acct.getId();
            getAccountOrCreate();
        } else {
            Toast.makeText(LoginActivity.this, R.string.login_toast_error_identification, Toast.LENGTH_LONG).show();
        }
    }

    public void onResponseFacebook(LoginResult loginResult) {
        account = new Account();
        account.fb_id = loginResult.getAccessToken().getUserId();
        getAccountOrCreate();
    }

    public void getAccountOrCreate() {
        APIHelper.getAccountOrCreate(LoginActivity.this, account, new TaskComplete<Account>() {
            @Override
            public void run() {
                Answer<Account> answer = this.result;
                if (answer.status < 300) {
                    account = answer.data;
                    Preference.setAccount(LoginActivity.this, account);
                    if (account.pseudo == null)
                        showPseudoLayout();
                    else {
                        next();
                    }
                }
                else
                    answer.message.displayMessage(LoginActivity.this);
            }
        });
    }

    public void showPseudoLayout(){
        final ScrollView linearLayout = (ScrollView) findViewById(R.id.layout_create_pseudo);
        Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animation);
    }

    public void createPseudo(View view) {
        closeKeyboard();
        String pseudo = ((EditText) findViewById(R.id.create_pseudo_edit)).getText().toString();

        if(!pseudo.isEmpty()) {
            account.pseudo = pseudo;
            APIHelper.updateAccount(LoginActivity.this, account, new TaskComplete<Account>() {
                @Override
                public void run() {
                    Answer<Account> answer = this.result;
                    if (answer.status < 300) {
                        account = answer.data;
                        Preference.setAccount(LoginActivity.this, account);
                        findViewById(R.id.layout_create_pseudo).setVisibility(View.GONE);
                        next();
                    }
                    else
                        answer.message.displayMessage(LoginActivity.this);
                }
            });
        }
        else{
            TextView textViewErr = (TextView) findViewById(R.id.error_create_pseudo_no_text);
            textViewErr.setVisibility(View.VISIBLE);
            textViewErr.setText( R.string.error_create_pseudo_no_text);
        }
    }

    public void quitCreatePseudo(View view) {
        closeKeyboard();
        EditText editText = (EditText) findViewById(R.id.create_pseudo_edit);
        editText.setText("");
        final ScrollView linearLayout = (ScrollView) findViewById(R.id.layout_create_pseudo);
        Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.startAnimation(animation);
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
