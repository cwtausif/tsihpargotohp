package com.glowingsoft.photographist.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.photographist.Activities.Main2Activity;
import com.glowingsoft.photographist.Fragments.ProfileFragment;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Model.UserModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell on 10/16/2017.
 */

public class SearchUserRecyclerView extends RecyclerView.Adapter<SearchUserRecyclerView.MyViewHolder> {
    Context context;
    ArrayList<UserModel> userModel;

    public SearchUserRecyclerView(ArrayList<UserModel> userModel) {
        this.userModel = userModel;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(context).load(userModel.get(position).getImage()).into(holder.userImageView);
        holder.userTV.setText(userModel.get(position).getName());
        holder.followersTV.setText(userModel.get(position).getFollowers() + " Followers");
        if (userModel.get(position).getIsFollowing() == 1) {
            holder.followButton.setText("Following");
        } else {
            holder.followButton.setText("Follow");
        }
        holder.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Fragment fragment = new ProfileFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user_id",userModel.get(position).getId());
                    fragment.setArguments(bundle);
                    Main2Activity.fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RequestParams params = new RequestParams();
                params.put("user_id", userModel.get(position).getId());
                params.put("follower_id", userModel.get(position).getRequested_user());
                if (userModel.get(position).getIsFollowing() == 1) {
                    params.put("is_followed", 2);
                    userModel.get(position).setIsFollowing(2);
                    if (userModel.get(position).getFollowers()>0)
                    userModel.get(position).setFollowers(userModel.get(position).getFollowers()-1);
                    notifyDataSetChanged();
                } else if (userModel.get(position).getIsFollowing() == 2) {
                    params.put("is_followed", 1);
                    userModel.get(position).setIsFollowing(1);
                    userModel.get(position).setFollowers(userModel.get(position).getFollowers()+1);
                    notifyDataSetChanged();
                }



                WebReq.post(context, ApiEndPoints.getInstance().followUnfollow, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//        holder.commentsTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, CommmentsActivity.class);
//                intent.putExtra("post_id", feedModels.get(position).getId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
//        holder.userImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Fragment fragment = new ProfileFragment();
//                    Bundle bundle=new Bundle();
//                    bundle.putString("user_id",feedModels.get(position).getUser_id());
//                    fragment.setArguments(bundle);
//                    Main2Activity.fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
//                } catch (Exception e) {
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        if (feedModels.get(position).getIslike() == "2") {
//            holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
//        }
//
//
//        holder.itemView.setOnClickListener(new DoubleClickListener() {
//
//            @Override
//            public void onSingleClick(View v) {
//
//            }
//
//            @Override
//            public void onDoubleClick(View v) {
//                RequestParams params = new RequestParams();
//
//                if (feedModels.get(position).getIslike() == "2") {
//
//                    holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filled_star, 0, 0, 0);
//                    feedModels.get(position).setIslike("1");
//                    feedModels.get(position).setLikes_count(feedModels.get(position).getLikes_count() + 1);
//                    notifyDataSetChanged();
//                    params.put("islike", "1");
//
//                } else {
//
//                    holder.likesTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.star, 0, 0, 0);
//                    feedModels.get(position).setIslike("2");
//                    feedModels.get(position).setLikes_count(feedModels.get(position).getLikes_count() - 1);
//                    notifyDataSetChanged();
//                    params.put("islike", "2");
//                    Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
//                }
//                holder.likesTV.setText(String.valueOf(feedModels.get(position).getLikes_count()) + " Likes");
//                try {
//                    params.put("post_id", feedModels.get(position).getId());
//                    params.put("user_id", AppActivity.getInstance().getUserID(context));
//                } catch (JSONException e) {
//                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//                Log.d(AppActivity.LOG, params.toString());
//                WebReq.post(context, ApiEndPoints.getInstance().likeunlike, params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        Log.d(AppActivity.LOG, response.toString());
//                        try {
//                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImageView;
        TextView userTV, followersTV;
        Button followButton;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            userImageView = itemView.findViewById(R.id.userImageView);
            userTV = itemView.findViewById(R.id.userTV);
            followersTV = itemView.findViewById(R.id.followersTV);
            followButton = itemView.findViewById(R.id.followButton);
        }
    }

}
