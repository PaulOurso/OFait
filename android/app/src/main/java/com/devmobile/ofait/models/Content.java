package com.devmobile.ofait.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Tony on 20/10/2016.
 */

public class Content {

    public String _id;
    public Account created_by;
    public String content_value;
    public String created_date;
    public List<Vote> votes;
    public int points;

    public static Type typeAnswerOf() {
        return new TypeToken<Answer<Content>>() {
        }.getType();
    }
}
