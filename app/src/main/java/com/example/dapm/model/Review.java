package com.example.dapm.model;

public class Review {
    private int userAvatar;
    private String userName;
    private String userComment;
    private String timestamp;
    private int productImage;
    private String productName;
    private String productPrice;

    public Review(int userAvatar, String userName, String userComment, String timestamp, int productImage, String productName, String productPrice) {
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.userComment = userComment;
        this.timestamp = timestamp;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
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

    public String getTimestamp() {
        return timestamp;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }
}

