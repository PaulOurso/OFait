package com.devmobile.ofait.utils;

/**
 * Created by Tony on 23/10/2016.
 */

public class IPConfig {
    /**
     * API
     */
    private static final int PORT_API = 9000;
    // Localhost
    public static final String DOMAIN_API = "http://192.168.1.19:"+String.valueOf(PORT_API)+"/api";
    // Distant
    //public static final String DOMAIN_API = "http://ofait.ddns.net:"+String.valueOf(PORT_API)+"/api";

    /**
     * SOCKET
     */
    private static final int PORT_SOCKET = 9001;
    // Localhost
    public static final String DOMAIN_SOCKET = "http://192.168.1.19:"+String.valueOf(PORT_SOCKET)+"/";
    // Distant
    //public static final String DOMAIN_SOCKET = "http://ofait.ddns.net:"+String.valueOf(PORT_SOCKET)+"/";
}
