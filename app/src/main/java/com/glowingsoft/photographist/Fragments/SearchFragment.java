package com.glowingsoft.photographist.Fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.glowingsoft.photographist.Activities.AppActivity;
import com.glowingsoft.photographist.Adapters.SearchUserRecyclerView;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.Model.UserModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchFragment extends ParentFragment {
    SearchView searchView;
    ArrayList<UserModel> userModelArrayList;
    private String follower_id;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity();
        recyclerView=view.findViewById(R.id.recyclerView);
        try {
            follower_id = AppActivity.getInstance().getUserID(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(AppActivity.LOG, s);
                refreshData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(AppActivity.LOG, s);
                return false;
            }
        });
        return view;
    }

    private void refreshData(String query) {

        RequestParams params = new RequestParams();
        params.put("follower_id", follower_id);
        params.put("query", query);
        Log.d(AppActivity.LOG, params.toString());
        WebReq.post(context, ApiEndPoints.getInstance().searchUser, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(AppActivity.LOG, response.toString());
                try {

                    int status = response.getInt("status");
                    userModelArrayList=new ArrayList<>();
                    if (status == Utils.STATUS) {
                        JSONArray resultArray = response.getJSONArray("result");
                        JSONObject itemResult;
                        for (int i = 0; i < resultArray.length(); i++) {
                            itemResult = resultArray.getJSONObject(i);
                            userModelArrayList.add(new UserModel(itemResult.getString("id"), itemResult.getString("email"), itemResult.getString("name"), Utils.IMAGE + itemResult.getString("image"), itemResult.getInt("isFollowing"), itemResult.getInt("followers"), itemResult.getString("requested_user")));
                        }
                        SearchUserRecyclerView searchUserRecyclerView = new SearchUserRecyclerView(userModelArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(searchUserRecyclerView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(AppActivity.LOG, throwable.toString());
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
