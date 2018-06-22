package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends ParentActivity {
    EditText emailET;
    Button forgotButton;
    String emailST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context=ForgotPasswordActivity.this;
        emailET = findViewById(R.id.forgotEmail);
        forgotButton = findViewById(R.id.forgotButton);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailST = emailET.getText().toString().trim();
                if (!invalidate(emailST)) {
                    requestForgotPassword(emailST);
                } else {
                    emailET.setError(getString(R.string.required));
                }
            }
        });
    }

    public void requestForgotPassword(String email) {
        params = new RequestParams();
        params.put("email", email);
        WebReq.post(context, ApiEndPoints.getInstance().forgotPassword, params, new WebRequest());
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
                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                if (status == Utils.STATUS) {
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
