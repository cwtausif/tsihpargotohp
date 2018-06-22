package com.glowingsoft.photographist.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.photographist.Model.FeedModel;
import com.glowingsoft.photographist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dell on 10/16/2017.
 */

public class PortfolioRecyclerView extends RecyclerView.Adapter<PortfolioRecyclerView.MyViewHolder> {
    ArrayList<FeedModel> feedModels;
    Context context;
    int id;

    public PortfolioRecyclerView(ArrayList<FeedModel> feedModels) {
        this.feedModels = feedModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(context).load(feedModels.get(position).getPost_image()).into(holder.postImageView);
        holder.likesTV.setText(String.valueOf(feedModels.get(position).getLikes_count()) + " Likes");
        holder.commentsTV.setText(String.valueOf(feedModels.get(position).getComments_count()) + " Comments");
    }

    @Override
    public int getItemCount() {
        return feedModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView postImageView;
        TextView userTV, likesTV, commentsTV;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            postImageView = itemView.findViewById(R.id.postImageView);
            userTV = itemView.findViewById(R.id.userTV);
            likesTV = itemView.findViewById(R.id.likesTV);
            commentsTV = itemView.findViewById(R.id.commentsTV);
        }
    }

}
