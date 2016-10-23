package com.devmobile.ofait.models;

import android.content.Context;
import android.util.Log;

import com.devmobile.ofait.utils.FastDialog;

/**
 * Created by MicroStop on 19/10/2016.
 */

public class Message {
    public String message;
    public String TAG = "Message Anwser";

    public void displayMessage(Context c) {
        if (message != null && c != null) {
            FastDialog.showDialog(c, FastDialog.SIMPLE_DIALOG, message);
        }
    }

    public void displayLog() {
        if (message != null)
            Log.d(TAG, message);
    }
}
