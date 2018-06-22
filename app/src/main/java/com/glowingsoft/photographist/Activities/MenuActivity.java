package com.glowingsoft.photographist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.glowingsoft.photographist.R;

public class MenuActivity extends ParentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        context = this;
        findViewById(R.id.logoutTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppActivity.getInstance().setLogin(context, false);
                startActivity(new Intent(context, SelectionActivity.class));
                ((Activity) context).finish();
            }
        });
        findViewById(R.id.termsTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, TermsActivity.class));
            }
        });
        findViewById(R.id.aboutTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AboutActivity.class));
            }
        });
    }
}
