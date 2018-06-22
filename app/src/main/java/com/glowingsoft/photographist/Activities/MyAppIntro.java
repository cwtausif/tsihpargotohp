package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentFive;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentFour;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentOne;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentSeven;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentSix;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentThree;
import com.glowingsoft.photographist.Fragments.IntroFragments.IntroFragmentTwo;
import com.glowingsoft.photographist.R;

public class MyAppIntro extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintro);
        init();
    }

    private void init() {

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return new IntroFragmentOne();
                case 1: return new IntroFragmentTwo();
                case 2: return new IntroFragmentThree();
                case 3: return new IntroFragmentFour();
                case 4: return new IntroFragmentFive();
                case 5: return new IntroFragmentSix();
                case 6: return new IntroFragmentSeven();
                default: return new IntroFragmentOne();
            }
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
