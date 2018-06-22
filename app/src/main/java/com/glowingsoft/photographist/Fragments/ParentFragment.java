package com.glowingsoft.photographist.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 5/5/2018.
 */

public class ParentFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipe;
    protected Context context;
    protected JSONObject jsonObject;
    protected int id;
    protected String url;
    TextView notFound;


    protected ImageView imageView;
    protected EditText descET,priceET,guestET;
    protected Button button;
    protected String p_price,p_description,p_guest;
    protected int p_category;
    protected Spinner spinner;
    protected ArrayList<String> spinnerList;


    protected void spinnerAdapter(Spinner spinner, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
