package com.devmobile.ofait.models;

import android.content.Context;

import com.devmobile.ofait.utils.FastDialog;

/**
 * Created by MicroStop on 19/10/2016.
 */

public class Message {
    public String message;

    public void displayMessage(Context c) {
        if (message != null) {
            FastDialog.showDialog(c, FastDialog.SIMPLE_DIALOG, message);
        }
    }
}
