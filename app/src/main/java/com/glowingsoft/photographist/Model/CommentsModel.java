package com.glowingsoft.photographist.Model;

/**
 * Created by Dell on 6/10/2018.
 */

public class CommentsModel {
    String post_id, comment, user_id, name, image;

    public CommentsModel(String post_id, String comment, String user_id, String name, String image) {
        this.post_id = post_id;
        this.comment = comment;
        this.user_id = user_id;
        this.name = name;
        this.image = image;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getComment() {
        return comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
