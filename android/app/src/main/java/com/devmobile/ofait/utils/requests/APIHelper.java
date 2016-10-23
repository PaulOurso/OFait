package com.devmobile.ofait.utils.requests;


import android.content.Context;

import com.android.volley.Request;
import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Content;
import com.devmobile.ofait.utils.IPConfig;

/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public class APIHelper {
    
    public static final String URL_MY_ACCOUNT = IPConfig.DOMAIN_API + "/find_my_account";
    public static final String URL_CREATE_ACCOUNT = IPConfig.DOMAIN_API + "/account";
    public static final String URL_ACCOUNT = IPConfig.DOMAIN_API + "/account/%s";
    public static final String URL_CREATE_CONTENT = IPConfig.DOMAIN_API + "/content";
    public static final String URL_ACCOUNT_STATS = IPConfig.DOMAIN_API + "/account/%s/stats";
    public static final String URL_GET_CONTENTS_TO_VOTE = IPConfig.DOMAIN_API + "/account/%s/contents_to_vote";

    public static void getAccountFromLogin(Context c, Account account, TaskComplete taskComplete) {
        getAccountFromLogin(c, false, account, taskComplete);
    }
    public static void getAccountFromLogin(Context c, boolean fromSplash, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, Account.typeAnswerOf(), taskComplete);
        apiRequest.setShowDialog(!fromSplash);
        String url = URL_MY_ACCOUNT;
        if (account.fb_id != null)
            url = URL_MY_ACCOUNT+"?fb_id="+account.fb_id;
        else if (account.google_id != null)
            url = URL_MY_ACCOUNT+"?google_id="+account.google_id;
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
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

    public static void createNewContent(Context c, Content newContent,TaskComplete taskComplete){
        APIRequest<Content> apiRequest =new APIRequest<>(c, Content.typeAnswerOf(), taskComplete);
        apiRequest.addParam("created_by", newContent.created_by._id);
        apiRequest.addParam("content_value", newContent.content_value);
        apiRequest.setMethod(Request.Method.POST);
        apiRequest.execute(URL_CREATE_CONTENT);
    }

    public static void getAccountStats(Context c, Account account, TaskComplete taskComplete) {
        APIRequest<Account> apiRequest = new APIRequest<>(c, Account.typeAnswerOf(), taskComplete);
        String url = String.format(URL_ACCOUNT_STATS, account._id);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.execute(url);
    }

    public static void getContentsToVote(Context c, Account account, TaskComplete taskComplete) {
        APIRequest<Content> apiRequest = new APIRequest<>(c, Content.typeAnswerOf(), taskComplete);
        String url = String.format(URL_GET_CONTENTS_TO_VOTE, account._id);
        apiRequest.setMethod(Request.Method.GET);
        apiRequest.setShowDialog(false);
        apiRequest.execute(url);
    }
}
