package com.example.cookstorm.model;

import java.util.List;

public class User {
    private String uid;
    private int photoImg;
    private String email;
    private String phoneNumber;
    private String address;
    private List<String> favoritePosts;
    private List<String> myPosts;

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getFavoritePosts() {
        return favoritePosts;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFavoritePosts(List<String> favoritePosts) {
        this.favoritePosts = favoritePosts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhotoImg(int photoImg) {
        this.photoImg = photoImg;
    }

    public int getPhotoImg() {
        return photoImg;
    }

    public List<String> getMyPosts() {
        return myPosts;
    }

    public String getFavoritePostSize() {
        if (favoritePosts != null) return String.valueOf(favoritePosts.size());
        return "0";
    }

    public String getMyPostsSize() {
        if (myPosts != null) return String.valueOf(myPosts.size());
        return "0";
    }
}
