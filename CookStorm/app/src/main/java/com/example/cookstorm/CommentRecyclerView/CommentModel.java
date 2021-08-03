package com.example.cookstorm.CommentRecyclerView;

public class CommentModel {

    int id;
    String name;
    String time;
    String commentText;
    String rankInfo;



    public CommentModel(int id, String name, String rankInfo, String time, String commentText) {
        this.id = id;
        this.name = name;
        this.rankInfo = rankInfo;
        this.time = time;
        this.commentText = commentText;
    }

    public int getId() {
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
