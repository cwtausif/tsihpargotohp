package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.view.View;

import com.glowingsoft.photographist.R;

public class SelectionActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        context = this;
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftNotFinish(context, LoginActivity.class);
            }
        });
        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftNotFinish(context, SignupActivity.class);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AppActivity.getInstance().isLogin(context)) {
            leftFinish(SelectionActivity.this, MainActivity.class);
        }
    }
}
