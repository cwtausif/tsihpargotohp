package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.view.View;

import com.glowingsoft.photographist.R;

public class MainActivity extends ParentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buyerBT:
                leftNotFinish(context,Main2Activity.class);
                break;
            case R.id.sellerBT:
                leftNotFinish(context,Main2Activity.class);
                break;
        }
    }
}
