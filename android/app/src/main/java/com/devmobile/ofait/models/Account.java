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
    public int remaining_contents;
    public List<Content> favorites_contents;
    public boolean notif;
    public int nb_votes;
    public int votes_by_content;
    public int votes_unused;
    public int reputation;
    public int next_lvl_reputation;
    public int previous_reputation;

    public static Type typeAnswerOf() {
        return new TypeToken<Answer<Account>>() {
        }.getType();
    }
}
