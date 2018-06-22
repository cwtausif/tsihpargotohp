package com.glowingsoft.photographist.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glowingsoft.photographist.Model.CommentsModel;
import com.glowingsoft.photographist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell on 10/16/2017.
 */

public class CommentsRecyclerView extends RecyclerView.Adapter<CommentsRecyclerView.MyViewHolder> {
    ArrayList<CommentsModel> commentsModels;
    Context context;
    int id;

    public CommentsRecyclerView(ArrayList<CommentsModel> commentsModels) {
        this.commentsModels = commentsModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(context).load(commentsModels.get(position).getImage()).into(holder.userImageView);
        holder.userTV.setText(commentsModels.get(position).getName());
        holder.commentTV.setText(commentsModels.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImageView;
        TextView userTV, commentTV;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            userImageView = itemView.findViewById(R.id.userImageView);
            userTV = itemView.findViewById(R.id.userTV);
            commentTV = itemView.findViewById(R.id.commentTV);
        }
    }

}
