package com.example.cookstorm.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Post {
    int likes, comments;
    String id, title, recipeField;
    String postPic; // note - a fake string, it's actually a bitmap
    String authorId;
    Date time;

    public Post() {
    }

    public Post(String id, int likes, int comments, String postPic, Date time, String title, String recipeField, String authorId) {
        this.id = id;
        this.likes = likes;
        this.comments = comments;
        this.postPic = postPic;
        this.time = time;
        this.title = title;
        this.recipeField = recipeField;
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) { this.comments = comments; }

    public String getPostPic() {
        return postPic;
    }

    public Date getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getRecipeField() {
        return recipeField;
    }

    public String getAuthorId() { return authorId; }

    public void likes() { this.likes++; }

    public void unlikes() { this.likes--; }
}
