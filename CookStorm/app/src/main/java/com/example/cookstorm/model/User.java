package com.example.cookstorm.model;

import java.util.List;

public class User {
    private String uid;
    private int photoImg;
    private String email;
    private String phoneNumber;
    private String address;
    private List<String> favoritePost;

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

    public List<String> getFavoritePost() {
        return favoritePost;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFavoritePost(List<String> favoritePost) {
        this.favoritePost = favoritePost;
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
}
