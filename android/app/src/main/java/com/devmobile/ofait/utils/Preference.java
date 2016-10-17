package com.devmobile.ofait.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.devmobile.ofait.models.Account;
import com.google.gson.Gson;

/**
 * Created by Jean-Noel on 29/09/2016.
 */
public class Preference {
    public static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    private static SharedPreferences get(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c);
    }

    private static String getPref(Context c, String key) {
        return get(c).getString(key, null);
    }

    private static void setPref(Context c, String key, String value) {
        get(c).edit().putString(key, value).apply();
    }

    public static Account getAccount(Context c) {
        String acc = getPref(c, Preference.KEY_ACCOUNT);
        Account account = null;
        if (acc != null) {
            Gson gson = new Gson();
            account = gson.fromJson(acc, Account.class);
        }
        return account;
    }

    public static void setAccount(Context c, Account account) {
        if (account != null)
            setPref(c, KEY_ACCOUNT, new Gson().toJson(account));
        else
            setPref(c, KEY_ACCOUNT, null);
    }
}