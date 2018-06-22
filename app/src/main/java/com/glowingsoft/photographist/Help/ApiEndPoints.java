package com.glowingsoft.photographist.Help;

/**
 * Created by Dell on 3/6/2018.
 */

public class ApiEndPoints {
    public final String loginURL = "login";
    public String getPosts = "getPosts";
    public String followUnfollow = "followUnfollow";
    public String searchUser = "searchUser";
    public String getPostsById = "getPostsById";
    public String getPostComments = "getPostComments";
    public String profile = "profile";
    public String postComment = "postComment";
    public String likeunlike = "likeunlike";
    public String updateProfilePicture = "updateProfilePicture";
    public String updateProfileCover = "updateProfileCover";
    public String updateAdditionalInfo = "updateAdditionalInfo";
    public String getOrder = "getOrder";
    public String review = "review";
    public String orderComplete = "orderComplete";
    public String saveOrder = "saveOrder";
    public String forgotPassword = "forgotPassword";
    public String updateSubscriptions = "updateSubscriptions";
    public String currencyURL = "http://free.currencyconverterapi.com/api/v5/convert?q=IDR_USD&compact=y";

    public static ApiEndPoints getInstance() {
        return new ApiEndPoints();
    }
}
