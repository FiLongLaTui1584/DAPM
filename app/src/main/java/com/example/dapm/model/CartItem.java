package com.example.dapm.model;

public class CartItem {
    private String name;
    private int imageRes;
    private String size;
    private String quantity;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public CartItem(String name, int imageRes, String size, String quantity, String price) {
        this.name = name;
        this.imageRes = imageRes;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }
}
