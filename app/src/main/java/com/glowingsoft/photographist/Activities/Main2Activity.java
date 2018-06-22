package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.glowingsoft.photographist.Fragments.NewsFeedFragment;
import com.glowingsoft.photographist.Fragments.NotificationFragment;
import com.glowingsoft.photographist.Fragments.ProfileFragment;
import com.glowingsoft.photographist.Fragments.SearchFragment;
import com.glowingsoft.photographist.R;

public class Main2Activity extends ParentActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new_feed:
                    replaceFragment(0);
                    return true;
                case R.id.navigation_search:
                    replaceFragment(1);
                    return true;
                case R.id.navigation_profile:
                    replaceFragment(2);
                    return true;
                case R.id.navigation_notification:
                    replaceFragment(3);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this;
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.inflateMenu(R.menu.menu);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        replaceFragment(0);
    }

    public void replaceFragment(int position) {
        switch (position) {
            case 0:
                fragment=new NewsFeedFragment();
                break;
            case 1:
                fragment = new SearchFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
            case 3:
                fragment = new NotificationFragment();
                break;
        }

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }
}
