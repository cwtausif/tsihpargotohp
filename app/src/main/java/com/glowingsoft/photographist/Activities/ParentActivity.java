package com.glowingsoft.photographist.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.glowingsoft.photographist.R;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * Created by Dell on 10/28/2017.
 */

public class ParentActivity extends AppCompatActivity {
    protected static final String TAG = "response";
    public Context context;
    EditText register_emailET, register_nameET, register_addressET, register_phoneET, register_passwordET;
    Spinner type_Spinner;
    RequestParams params;
    Resources resources;
    Fragment fragment;
    public static FragmentManager fragmentManager;


    CallbackManager callbackManager;
    LoginButton loginButton;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    String url,user_id;

    public static ParentActivity getInstance() {
        return new ParentActivity();
    }

    protected void spinneradapter(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void leftFinish(Context context, Class to) {
        context.startActivity(new Intent(context, to));
        ((Activity) context).finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    protected void leftNotFinish(Context context, Class to) {
        context.startActivity(new Intent(context, to));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void rightFinish(Context context, Class to) {
        context.startActivity(new Intent(context, to));
        ((Activity) context).finish();
        overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
    }

    protected void startNewActivity(Context context, Class to) {
        context.startActivity(new Intent(context, to));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected boolean invalidate(String string) {
        if (TextUtils.isEmpty(string))
            return true;
        else
            return false;
    }

    protected String get(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void error(EditText editText, Resources resources) {
        editText.setError(resources.getString(R.string.required));
    }

    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
