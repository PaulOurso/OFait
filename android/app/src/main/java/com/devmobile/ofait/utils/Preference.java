package com.devmobile.ofait.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
