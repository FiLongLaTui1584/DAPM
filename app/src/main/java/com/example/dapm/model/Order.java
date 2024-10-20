package com.example.dapm.model;

public class Order {
    private String name;
    private int imageResId;  // Image resource ID (int type)
    private String income;
    private String date;

    public Order(String name, int imageResId, String income, String date) {
        this.name = name;
        this.imageResId = imageResId;
        this.income = income;
        this.date = date;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


