package com.glowingsoft.photographist.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.photographist.Activities.AppActivity;
import com.glowingsoft.photographist.Activities.CommmentsActivity;
import com.glowingsoft.photographist.Activities.Main2Activity;
import com.glowingsoft.photographist.Fragments.ProfileFragment;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.DoubleClickListener;
import com.glowingsoft.photographist.Model.FeedModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell on 10/16/2017.
 */

public class NewsFeedRecyclerView extends RecyclerView.Adapter<NewsFeedRecyclerView.MyViewHolder> {
    ArrayList<FeedModel> feedModels;
    Context context;

    public NewsFeedRecyclerView(ArrayList<FeedModel> feedModels) {
        this.feedModels = feedModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(context).load(feedModels.get(position).getPost_image()).into(holder.postImageView);
        Picasso.with(context).load(feedModels.get(position).getUser_image()).into(holder.userImageView);
        holder.userTV.setText(feedModels.get(position).getUploaded_by());
        holder.likesTV.setText(String.valueOf(feedModels.get(position).getLikes_count()) + " Likes");
        holder.commentsTV.setText(String.valueOf(feedModels.get(position).getComments_count()) + " Comments");


        holder.commentsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommmentsActivity.class);
                intent.putExtra("post_id", feedModels.get(position).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Fragment fragment = new ProfileFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user_id",feedModels.get(position).getUser_id());
                    fragment.setArguments(bundle);
                    Main2Activity.fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Objects.equals(feedModels.get(position).getIslike(), "2")) {
            holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
        }


        holder.itemView.setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                RequestParams params = new RequestParams();

                if (Objects.equals(feedModels.get(position).getIslike(), "2")) {

                    holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filled_star, 0, 0, 0);
                    feedModels.get(position).setIslike("1");
                    feedModels.get(position).setLikes_count(feedModels.get(position).getLikes_count() + 1);
                    notifyDataSetChanged();
                    params.put("islike", "1");

                } else {
                    holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
                    feedModels.get(position).setIslike("2");
                    feedModels.get(position).setLikes_count(feedModels.get(position).getLikes_count() - 1);
                    notifyDataSetChanged();
                    params.put("islike", "2");
                    Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                }
                holder.likesTV.setText(String.valueOf(feedModels.get(position).getLikes_count()) + " Likes");
                try {
                    params.put("post_id", feedModels.get(position).getId());
                    params.put("user_id", AppActivity.getInstance().getUserID(context));
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.d(AppActivity.LOG, params.toString());
                WebReq.post(context, ApiEndPoints.getInstance().likeunlike, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d(AppActivity.LOG, response.toString());
                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImageView, shareTV;
        CircleImageView userImageView;
        TextView userTV, likesTV, commentsTV;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            postImageView = itemView.findViewById(R.id.postImageView);
            userImageView = itemView.findViewById(R.id.userImageView);
            userTV = itemView.findViewById(R.id.userTV);
            likesTV = itemView.findViewById(R.id.likesTV);
            commentsTV = itemView.findViewById(R.id.commentsTV);
            shareTV = itemView.findViewById(R.id.shareTV);
        }
    }

}
