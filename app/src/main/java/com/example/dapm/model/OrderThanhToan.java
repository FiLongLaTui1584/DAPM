package com.example.dapm.model;

public class OrderThanhToan {
    private int imageResource;
    private String name;
    private String price;
    private int quantity;

    public OrderThanhToan(int imageResource, String name, String price, int quantity) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

