package com.example.dapm.model;

public class Product {
    private int imageResId;
    private String title;
    private String price;
    private String date;
    private String location;

    public Product(int imageResId, String title, String price, String date, String location) {
        this.imageResId = imageResId;
        this.title = title;
        this.price = price;
        this.date = date;
        this.location = location;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
