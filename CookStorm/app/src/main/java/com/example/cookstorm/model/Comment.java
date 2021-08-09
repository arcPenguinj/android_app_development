package com.example.cookstorm.model;

public class Comment {

    String id;
    String name;
    String time;
    String commentText;
    String rankInfo;

    public Comment() {

    }

    public Comment(String id, String name, String rankInfo, String time, String commentText) {
        this.id = id;
        this.name = name;
        this.rankInfo = rankInfo;
        this.time = time;
        this.commentText = commentText;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getRankInfo() {
        return rankInfo;
    }
}
