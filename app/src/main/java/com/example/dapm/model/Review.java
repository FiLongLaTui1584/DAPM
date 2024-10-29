package com.example.dapm.model;

public class Review {
    private String reviewerID;
    private String reviewContent;
    private int rate;

    public Review() {}

    public Review(String reviewerID, String reviewContent, int rate) {
        this.reviewerID = reviewerID;
        this.reviewContent = reviewContent;
        this.rate = rate;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
