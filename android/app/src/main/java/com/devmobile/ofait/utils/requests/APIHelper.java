package com.devmobile.ofait.utils.requests;


/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public class APIHelper {
    // Localhost
    public static final String DOMAIN = "http://192.168.20.160:9000/api";
    // Access server
    //public static final String DOMAIN = "http://ofait.ddns.net/api";

    public static final String URL_ACCOUNTS = DOMAIN + "/accounts";
    public static final String URL_JOIN_GROUP = DOMAIN + "/accounts/%1$s/groups/rel/%2$s";

}
