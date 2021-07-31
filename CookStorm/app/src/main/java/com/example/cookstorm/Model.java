package com.example.cookstorm;

public class Model {


    int id, likes, comments, proPic, postPic;
    String name, time, title, rankInfo, recipeField;

    public Model(int id, int likes, int comments, int proPic, int postPic, String name, String time, String title, String rankInfo, String recipeField) {
        this.id = id;
        this.likes = likes;
        this.comments = comments;
        this.proPic = proPic;
        this.postPic = postPic;
        this.name = name;
        this.time = time;
        this.title = title;
        this.rankInfo = rankInfo;
        this.recipeField = recipeField;
    }

    public int getId() {
        return id;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public int getProPic() {
        return proPic;
    }

    public int getPostPic() {
        return postPic;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getRankInfo() {
        return rankInfo;
    }
    public String getRecipeField() {
        return recipeField;
    }
}
