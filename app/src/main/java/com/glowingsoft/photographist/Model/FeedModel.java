package com.glowingsoft.photographist.Model;

import java.util.ArrayList;

/**
 * Created by Dell on 6/10/2018.
 */

public class FeedModel {
    private String id,user_id,post_image,created_on,user_image,uploaded_by,islike;
    private  int likes_count,comments_count;
    private ArrayList<CommentsModel> commentsModels;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getUser_image() {
        return user_image;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public ArrayList<CommentsModel> getCommentsModels() {
        return commentsModels;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public String getIslike() {
        return islike;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public void setCommentsModels(ArrayList<CommentsModel> commentsModels) {
        this.commentsModels = commentsModels;
    }

    public FeedModel(String id, String user_id, String post_image, String created_on, String user_image, String uploaded_by, String islike, int likes_count, int comments_count, ArrayList<CommentsModel> commentsModels) {

        this.id = id;
        this.user_id = user_id;
        this.post_image = post_image;
        this.created_on = created_on;
        this.user_image = user_image;
        this.uploaded_by=uploaded_by;
        this.islike=islike;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.commentsModels = commentsModels;
    }
}
