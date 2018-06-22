package com.glowingsoft.photographist.Model;

/**
 * Created by Dell on 6/12/2018.
 */

public class UserModel {
    String id,email,name,image,requested_user;
    int isFollowing,followers;

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getRequested_user() {
        return requested_user;
    }

    public void setRequested_user(String requested_user) {
        this.requested_user = requested_user;
    }

    public UserModel(String id, String email, String name, String image, int isFollowing,int followers, String requested_user) {

        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
        this.isFollowing = isFollowing;
        this.followers = followers;
        this.requested_user = requested_user;
    }
}
