package com.devmobile.ofait.utils.requests;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devmobile.ofait.R;
import com.devmobile.ofait.models.Answer;
import com.devmobile.ofait.models.Message;
import com.devmobile.ofait.utils.FastDialog;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Tony Wisniewski on 10/10/2016.
 */
public class APIRequest<TypeData> {

    private static final String TAG = "APIRequest";
    private Context context;
    private HashMap<String, String> params;
    private boolean displayProgressDialog;
    private Dialog dialog;
    private TaskComplete taskComplete;
    private int method;
    private final static String[] METHOD_LOG = new String[] {"GET", "POST", "PUT", "DELETE"};
    private Answer<TypeData> answer;
    private Type resultClass;

    public APIRequest(Context c, Type resClass, TaskComplete taskCpl) {
        context = c;
        params = new HashMap<>();
        displayProgressDialog = true;
        dialog = null;
        taskComplete = taskCpl;
        method = -99999;
        answer = new Answer<>();
        resultClass = resClass;
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    private HashMap<String, String> getParams() {
        return params;
    }

    public void setShowDialog(boolean dialog) {
        displayProgressDialog = dialog;
    }

    public void setMethod(int meth) {
        method = meth;
    }

    public void execute(final String url) {
        if (method == -99999) {
            Log.e("TripCount APIRequest", "Method request missing ! (url: "+url+")");
            return;
        }
        if (!Network.isOnline(context)) {
            Toast.makeText(context, R.string.not_connect, Toast.LENGTH_SHORT).show();
            return;
        }
        showLog(url);
        RequestQueue queue = Volley.newRequestQueue(context);
        showDialog();
        StringRequest stringRequest = new StringRequest(method, url, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.d(TAG+" RESPONSE ("+METHOD_LOG[method]+")", "(url: "+url+") -> "+response);
                answer = gson.fromJson(response, resultClass);
                if (taskComplete != null) {
                    taskComplete.result = answer;
                    taskComplete.run();
                }
                dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                NetworkResponse response = error.networkResponse;
                String res = null;
                if (response != null) {
                    if (response.data != null) {
                        try {
                            res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        answer = gson.fromJson(res, resultClass);
                        if (taskComplete != null) {
                            taskComplete.result = answer;
                            taskComplete.run();
                        }
                    } else if (response.statusCode == 404) {
                        Message message = new Message();
                        message.message = context.getString(R.string.error_request_server);
                        message.displayMessage(context);
                    }
                } else {
                    Message message = new Message();
                    message.message = context.getString(R.string.error_request_no_response);
                    message.displayMessage(context);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return APIRequest.this.getParams();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void showDialog() {
        if (displayProgressDialog) {
            dialog = FastDialog.showProgressDialog(context, R.string.loading);
            dialog.show();
        }
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void showLog(String url) {
        String log = url + " params: {";
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (i>0)
                log +=", ";
            log += entry.getKey()+": "+entry.getValue();
            i++;
        }
        log += "}";
        Log.d(TAG+" REQUEST ("+METHOD_LOG[method]+")", log);
    }
}
