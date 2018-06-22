package com.glowingsoft.photographist.Fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.glowingsoft.photographist.Activities.AppActivity;
import com.glowingsoft.photographist.Adapters.NewsFeedRecyclerView;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.Model.CommentsModel;
import com.glowingsoft.photographist.Model.FeedModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NewsFeedFragment extends ParentFragment {
    ArrayList<FeedModel> feedModels;
    ArrayList<CommentsModel> commentsModels;

    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        swipe = view.findViewById(R.id.swipe);
        context = getActivity();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            refreshData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    private void refreshData() throws JSONException {

        url = ApiEndPoints.getInstance().getPosts;
        Log.d(AppActivity.LOG, url);
        RequestParams params = new RequestParams();
        params.put("user_id", AppActivity.getInstance().getUserID(context));
        WebReq.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                swipe.setRefreshing(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipe.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d(AppActivity.LOG, response.toString());
                    feedModels = new ArrayList<>();
                    commentsModels = new ArrayList<>();
                    int status = response.getInt("status");
                    if (status == Utils.STATUS) {
                        JSONArray postsArray = response.getJSONArray("posts");
                        JSONArray commentsArray;
                        JSONObject itemPost, itemComment;
                        for (int i = 0; i < postsArray.length(); i++) {


                            itemPost = postsArray.getJSONObject(i);
                            commentsArray = itemPost.getJSONArray("comments");

                            for (int j = 0; j < commentsArray.length(); j++) {

                                itemComment = commentsArray.getJSONObject(j);
                                commentsModels.add(new CommentsModel(itemComment.getString("post_id"), itemComment.getString("comment"), itemComment.getString("user_id"), itemComment.getString("name"), Utils.IMAGE + itemComment.getString("image")));
                            }
                            feedModels.add(new FeedModel(itemPost.getString("id"), itemPost.getString("user_id"), Utils.IMAGE + itemPost.getString("post_image"), itemPost.getString("created_on"), Utils.IMAGE + itemPost.getString("user_image"), itemPost.getString("uploaded_by"), itemPost.getString("islike"), itemPost.getInt("likes_count"), itemPost.getInt("comments_count"), commentsModels));
                        }
                        NewsFeedRecyclerView newsFeedRecyclerView = new NewsFeedRecyclerView(feedModels);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(newsFeedRecyclerView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
