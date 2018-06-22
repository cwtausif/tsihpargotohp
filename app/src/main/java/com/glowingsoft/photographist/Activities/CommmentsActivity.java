package com.glowingsoft.photographist.Activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.glowingsoft.photographist.Adapters.CommentsRecyclerView;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.Model.CommentsModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommmentsActivity extends ParentActivity implements View.OnClickListener {
    ArrayList<CommentsModel> commentsModels;
    String post_id;
    EditText commentET;
    JSONObject jsonObject;
    String name, image;
    private String commentST;
    private CommentsRecyclerView commentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commments);
        recyclerView = findViewById(R.id.recyclerView);
        commentET = findViewById(R.id.commentET);
        swipe = findViewById(R.id.swipe);
        context = this;

        try {
            jsonObject = new JSONObject(AppActivity.getInstance().getUser(context));
            post_id = getIntent().getStringExtra("post_id");
            user_id = AppActivity.getInstance().getUserID(context);
            name = jsonObject.getString("name");
            image = jsonObject.getString("image");
            image = Utils.IMAGE + image;
        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
    }

    private void refreshData() throws JSONException {
        RequestParams params = new RequestParams();
        params.put("post_id", post_id);
        WebReq.post(context, ApiEndPoints.getInstance().getPostComments, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                swipe.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d(AppActivity.LOG, response.toString());
                    commentsModels = new ArrayList<>();
                    int status = response.getInt("status");
                    if (status == Utils.STATUS) {
                        JSONArray commentsArray;
                        JSONObject itemComment;
                        commentsArray = response.getJSONArray("comments");

                        for (int j = 0; j < commentsArray.length(); j++) {

                            itemComment = commentsArray.getJSONObject(j);
                            commentsModels.add(new CommentsModel(itemComment.getString("post_id"), itemComment.getString("comment"), itemComment.getString("user_id"), itemComment.getString("name"), Utils.IMAGE + itemComment.getString("image")));
                        }

                        commentsRecyclerView = new CommentsRecyclerView(commentsModels);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(commentsRecyclerView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipe.setRefreshing(false);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        commentST = get(commentET);
        if (!invalidate(commentST)) {
            requestPost(commentST);
            commentET.setText("");
        } else {
            error(commentET, getResources());
        }
    }

    public void requestPost(String comment) {
        params = new RequestParams();
        params.put("comment", comment);
        params.put("user_id", user_id);
        params.put("post_id", post_id);
        WebReq.post(context, ApiEndPoints.getInstance().postComment, params, new WebRequest());
    }

    private class WebRequest extends JsonHttpResponseHandler {
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
                int status = response.getInt("status");
                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                if (status == Utils.STATUS) {
                   refreshData();
                }
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
