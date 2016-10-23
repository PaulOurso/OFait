package com.devmobile.ofait.utils.sockets;

import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Tony on 22/10/2016.
 */

public class SocketManager {

    //public static final String DOMAIN = "http://ofait.ddns.net/api";
    public static final String DOMAIN = "http://192.168.1.14:9001/";
    private static SocketManager mSocketManager;

    private Socket mSocket;
    private MainActivity mActivity;

    public static SocketManager getInstance(MainActivity activity) {
        if (mSocketManager == null)
            mSocketManager = new SocketManager(activity);
        return mSocketManager;
    }


    private SocketManager(MainActivity activity) {
        mActivity = activity;
        try {
            mSocket = IO.socket(DOMAIN);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        mSocket.connect();
    }

    public void disconnect() {
        if (mSocket != null)
            mSocket.disconnect();
    }
}
