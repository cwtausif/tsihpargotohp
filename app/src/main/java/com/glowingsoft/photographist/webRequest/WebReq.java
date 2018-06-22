package com.glowingsoft.photographist.webRequest;

import android.content.Context;
import android.util.Log;
import com.glowingsoft.photographist.Help.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class WebReq {

    public static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
    }

    public static void get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.get(context, getAbsoluteUrl(url), params, responseHandler);
        Log.d("URL",getAbsoluteUrl(url));
    }
    public static void cancelRequest(){
        client.cancelAllRequests(true);
        Log.d("URL","Request Canceled");
    }
    public static void post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(context, getAbsoluteUrl(url), params, responseHandler);
        Log.d("URL",getAbsoluteUrl(url));
    }
    public static void getlocation(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(context, url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Utils.SERVER + relativeUrl;
    }
}
