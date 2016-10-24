package com.devmobile.ofait.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 17/10/2016.
 */
public class Account {

    public String _id;
    public String pseudo;
    public String google_id;
    public String fb_id;
    public int votesSpent;
    public int reputation;
    public List<Vote> votes;
    public int remaining_contents;
    public boolean notif;

    public static Type typeAnswerOf() {
        return new TypeToken<Answer<Account>>() {
        }.getType();
    }
}
