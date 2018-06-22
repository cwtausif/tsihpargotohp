package com.glowingsoft.photographist.Fragments.IntroFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glowingsoft.photographist.Activities.SelectionActivity;
import com.glowingsoft.photographist.R;


public class IntroFragmentSeven extends Fragment {
    TextView startTV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_intro_fragment_seven, container, false);
        startTV=view.findViewById(R.id.startTV);
        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SelectionActivity.class));
                ((Activity) getContext()).finish();
            }
        });
        return view;
    }
}
