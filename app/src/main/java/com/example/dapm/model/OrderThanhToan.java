package com.example.dapm.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderThanhToan implements Parcelable {
    private String imageResource;
    private String name;
    private int price;
    private int quantity;

    public OrderThanhToan(String imageResource, String name, int price, int quantity) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    protected OrderThanhToan(Parcel in) {
        imageResource = in.readString();
        name = in.readString();
        price = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<OrderThanhToan> CREATOR = new Creator<OrderThanhToan>() {
        @Override
        public OrderThanhToan createFromParcel(Parcel in) {
            return new OrderThanhToan(in);
        }

        @Override
        public OrderThanhToan[] newArray(int size) {
            return new OrderThanhToan[size];
        }
    };

    public String getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageResource);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(quantity);
    }
}
