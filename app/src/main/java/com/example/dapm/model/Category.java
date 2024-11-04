package com.example.dapm.model;

public class Category {
    private String categoryName;
    private String categoryImageURL;
    private String categoryID;

    public Category() {
    }

    public Category(String categoryName, String categoryImageURL, String categoryID) {
        this.categoryName = categoryName;
        this.categoryImageURL = categoryImageURL;
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImageURL() {
        return categoryImageURL;
    }

    public String getCategoryID() {
        return categoryID;
    }
}
