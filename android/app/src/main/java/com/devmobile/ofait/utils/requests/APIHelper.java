package com.devmobile.ofait.utils.requests;


import android.content.Context;

import com.android.volley.Request;
import com.devmobile.ofait.models.Account;

/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public class APIHelper {
    // Localhost
    public static final String DOMAIN = "http://192.168.20.160:9000/api";
    // Access server
    //public static final String DOMAIN = "http://ofait.ddns.net/api";

    public static final String URL_MY_ACCOUNT = DOMAIN + "/find_my_account";
    public static final String URL_CREATE_ACCOUNT = DOMAIN + "/account";
    public static final String URL_ACCOUNT = DOMAIN + "/account/%s";
    public static final String URL_JOIN_GROUP = DOMAIN + "/accounts/%1$s/groups/rel/%2$s";

    public static void getAccountFromLogin(Context c, Account account, TaskComplete taskComplete) {
        getAccountFromLogin(c, false, account, taskComplete);
    }
    public static void getAccountFromLogin(Context c, boolean fromSplash, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, Account.typeAnswerOf(), taskComplete);
        apiRequest.setShowDialog(!fromSplash);
        if (account.fb_id != null)
            apiRequest.addParam("fb_id", account.fb_id);
        else if (account.google_id != null)
            apiRequest.addParam("google_id", account.google_id);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(URL_MY_ACCOUNT);
    }

    public static void getAccountOrCreate(Context c, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, Account.typeAnswerOf(), taskComplete);
        if (account.fb_id != null)
            apiRequest.addParam("fb_id", account.fb_id);
        else if (account.google_id != null)
            apiRequest.addParam("google_id", account.google_id);
        apiRequest.setMethod(Request.Method.POST);
        apiRequest.execute(URL_CREATE_ACCOUNT);
    }

    public static void updateAccount(Context c, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, Account.typeAnswerOf(), taskComplete);
        String url = String.format(URL_ACCOUNT, account._id);
        apiRequest.addParam("pseudo", account.pseudo);
        apiRequest.setMethod(Request.Method.PUT);
        apiRequest.execute(url);
    }
}
