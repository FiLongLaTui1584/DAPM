package com.example.dapm.model;

public class DSChat {
    private String username;
    private String message;
    private int profileImage;

    public DSChat(String username, String message, int profileImage) {
        this.username = username;
        this.message = message;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public int getProfileImage() {
        return profileImage;
    }
}
