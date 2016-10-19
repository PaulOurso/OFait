package com.devmobile.ofait.utils.requests;


import android.content.Context;

import com.android.volley.Request;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Answer;

/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public class APIHelper {
    // Localhost
    public static final String DOMAIN = "http://192.168.20.160:9000/api";
    // Access server
    //public static final String DOMAIN = "http://ofait.ddns.net/api";

    public static final String URL_MY_ACCOUNTS = DOMAIN + "/my_account";
    public static final String URL_JOIN_GROUP = DOMAIN + "/accounts/%1$s/groups/rel/%2$s";

    public static void getAccountFromLogin(Context c, Account account, TaskComplete taskComplete) {
        getAccountFromLogin(c, false, account, taskComplete);
    }
    public static void getAccountFromLogin(Context c, boolean fromSplash, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, taskComplete);
        apiRequest.setShowDialog(!fromSplash);
        if (account.fb_id != null)
            apiRequest.addParam("fb_id", account.fb_id);
        else if (account.google_id != null)
            apiRequest.addParam("google_id", account.google_id);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(URL_MY_ACCOUNTS);
    }

}
