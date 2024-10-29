package com.example.dapm.model;

public class Review {
    private int userAvatar;
    private String userName;
    private String userComment;

    public Review(int userAvatar, String userName, String userComment) {
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.userComment = userComment;
    }

    // Getters and setters for all fields
    public int getUserAvatar() {
        return userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserComment() {
        return userComment;
    }
}

