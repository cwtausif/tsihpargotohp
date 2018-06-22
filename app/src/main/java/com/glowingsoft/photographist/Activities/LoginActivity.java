package com.glowingsoft.photographist.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends ParentActivity implements View.OnClickListener {
    private EditText login_emailET;
    private EditText login_passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.facebook_login);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                requestUserProfile(loginResult);
            }

            @Override
            public void onCancel() {
                Log.e("cancel", "cancel = true");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("error", "error = " + error.getMessage());
            }
        });

        context = this;
        login_emailET = findViewById(R.id.loginEmail);
        login_passwordET = findViewById(R.id.loginPassword);

        //region permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TedPermission.with(this).setPermissionListener(new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    finish();
                }
            })
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check();
        }
        //endregion
    }

    public void requestUserProfile(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                if (object != null) {
                    String pic = "";
                    try {
                        JSONObject picture = object.getJSONObject("picture");
                        if (picture != null) {
                            JSONObject data = picture.getJSONObject("data");
                            if (data != null)
                                pic = data.getString("url");
                        }
                        requestSocialLogin(object.getString("email"), object.getString("name"), "Facebook321#@!", object.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,gender,name,email,picture.type(large)"); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void requestSocialLogin(String email, String name, String password, String social_id) {
        params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("social_type", "facebook");
        params.put("device_id", getDeviceId(context));
        params.put("name", name);
        params.put("social_id", social_id);
        WebReq.post(context, ApiEndPoints.getInstance().loginURL, params, new WebRequest());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBT:
                leftFinish(context, SignupActivity.class);
                break;
            case R.id.forgotTV:
                leftNotFinish(context, ForgotPasswordActivity.class);
                break;
            case R.id.loginButton: {
                String loginEmail, loginPassword;
                loginEmail = get(login_emailET);
                loginPassword = get(login_passwordET);

                if (invalidate(loginEmail)) {
                    error(login_emailET, resources);
                }
                if (invalidate(loginPassword)) {
                    error(login_passwordET, resources);
                }
                if (!invalidate(loginPassword) && !invalidate(loginEmail)) {
                    requestWebLogin(loginEmail, loginPassword, "normal");
                }
            }

            break;
        }
    }

    public void requestWebLogin(String email, String password, String social_type) {
        params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("social_type", social_type);
        params.put("device_id", getDeviceId(context));
        params.put("devicetype", "android");
        WebReq.post(context, ApiEndPoints.getInstance().loginURL, params, new WebRequest());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class WebRequest extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                int status = response.getInt("status");
                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                if (status == Utils.STATUS) {
                    JSONObject user = response.getJSONObject("object");
                    AppActivity.getInstance().setLogin(context, true);
                    AppActivity.getInstance().saveUser(context, user);
                    leftFinish(context, MainActivity.class);
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
