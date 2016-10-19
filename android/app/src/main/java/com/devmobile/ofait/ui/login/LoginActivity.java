package com.devmobile.ofait.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.ui.categories.CategoriesActivity;
import com.devmobile.ofait.utils.Constant;
import com.devmobile.ofait.utils.Preference;
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

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = new Account();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

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

        SignInButton button = (SignInButton) findViewById(R.id.google_sign_in_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("clicked");
                    googleSignIn();
                }
            });
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        account = new Account();
                        account.fb_id = loginResult.getAccessToken().getUserId();

                        //todo: demander au serveur si le compte est deja crée

                        Preference.setAccount(LoginActivity.this,account);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void googleSignIn() {
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
            handleSignInResult(result);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            account.google_id = acct.getId();

            //todo: demander au serveur si le compte est deja crée

            Preference.setAccount(LoginActivity.this,account);

        } else {
            Toast.makeText(LoginActivity.this, R.string.login_toast_error_identification, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    private void showPseudoLayout(){
        findViewById(R.id.layout_create_pseudo).setVisibility(View.VISIBLE);
    }

    public void createPseudo(View view) {
        String pseudo= ((EditText) findViewById(R.id.create_pseudo_edit)).getText().toString();

        if(!pseudo.isEmpty()) {
            //todo: requete pour ajouter le pseudo au compte

            account.pseudo = pseudo;
            Preference.setAccount(LoginActivity.this,account);

            findViewById(R.id.layout_create_pseudo).setVisibility(View.GONE);
            CategoriesActivity.show(LoginActivity.this);
        }
        else{
            TextView textViewErr = (TextView) findViewById(R.id.error_create_pseudo_no_text);
            textViewErr.setVisibility(View.VISIBLE);
            textViewErr.setText( R.string.error_create_pseudo_no_text);
        }
    }

    public void quitCreatePseudo(View view) {
        EditText editText = (EditText) findViewById(R.id.create_pseudo_edit);
        editText.setText("");
        findViewById(R.id.layout_create_pseudo).setVisibility(View.GONE);
    }
}
