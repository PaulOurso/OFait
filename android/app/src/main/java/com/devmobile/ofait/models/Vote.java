package com.devmobile.ofait.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by MicroStop on 20/10/2016.
 */

public class Vote {

    public String _id;
    public String account_id;
    public String content_id;
    public int value;
    public String created_date;

    public static Type typeAnswerOf() {
        return new TypeToken<Answer<Vote>>() {
        }.getType();
    }
}
