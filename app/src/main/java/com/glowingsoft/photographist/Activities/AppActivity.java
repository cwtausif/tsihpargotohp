package com.glowingsoft.photographist.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.glowingsoft.photographist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
public class AppActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public String userObject = "userObject";
    private String shared_preferences_name = "eOrganizer";
    private String login = "login";
    private String user = "user";
    private String ip = "ip";
    private String type="type";
    Context context;
    public static String LOG="response";

    public static AppActivity getInstance() {
        return new AppActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }
    protected void leftFinish(Context context, Class to) {
        context.startActivity(new Intent(context, to));
        ((Activity) context).finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    public void setLogin(Context context, boolean action) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (action)
            editor.putBoolean(login, true);
        else
            editor.putBoolean(login, false);
        editor.apply();
    }

    public boolean isLogin(Context context) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(login, false);
    }

    public String getUser(Context context) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(this.userObject, "");
    }

    public void saveUser(Context context,JSONObject jsonObject) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(this.userObject, jsonObject.toString());
        editor.apply();
    }

    public String getUserID(Context context) throws JSONException {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        return new JSONObject(sharedPreferences.getString(this.userObject,"")).getString("id");

    }

    public void setType(Context context, int type) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(this.type, type);
        editor.apply();
    }
    public int getType(Context context) {
        sharedPreferences = context.getSharedPreferences(shared_preferences_name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(this.type, 0);
    }
    public String getDate() {
        String currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());
        return currentDateTimeString;
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }

        return false;
    }
}
