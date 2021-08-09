package com.example.cookstorm.model;

import java.util.ArrayList;
import java.util.List;

public class PostComments {
    private List<Comment> comments;
    private String postId;

    public PostComments() {}

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) comments = new ArrayList<>();
        comments.add(comment);
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
