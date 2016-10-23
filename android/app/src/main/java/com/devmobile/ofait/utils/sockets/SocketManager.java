package com.devmobile.ofait.utils.sockets;

import com.devmobile.ofait.models.Account;
import com.devmobile.ofait.models.Vote;
import com.devmobile.ofait.ui.mainmenu.MainActivity;
import com.devmobile.ofait.utils.IPConfig;
import com.devmobile.ofait.utils.Preference;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Tony on 22/10/2016.
 */

public class SocketManager {

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
            mSocket = IO.socket(IPConfig.DOMAIN_SOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initSocket(Account account) {
        mSocket.emit("init_socket", account._id);
    }

    public void connect() {
        if (!mSocket.connected()) {
            mSocket.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Account account = Preference.getAccount(mActivity);
                    initSocket(account);
                }
            });
            mSocket.on("voted_for_me", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            if (data !=  null) {
                                Gson gson = new Gson();
                                Vote vote = gson.fromJson(data.toString(), Vote.class);
                                mActivity.displayVoteForMe(vote);
                            }
                        }
                    });
                }
            });
            mSocket.connect();
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("connect");
        }
    }

    public void actionVote(Vote vote) {
        JSONObject jsonVote = new JSONObject();
        try {
            jsonVote.put("account", vote.account._id);
            jsonVote.put("content", vote.content._id);
            jsonVote.put("value", vote.value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("vote", jsonVote);
    }
}
