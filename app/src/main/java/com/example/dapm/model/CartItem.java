package com.example.dapm.model;

public class CartItem {
    private String productID;
    private String cartProductName;
    private String cartProductImage;
    private int cartProductPrice;
    private int cartQuantity;
    private boolean selected;

    public CartItem(String productID, String cartProductName, String cartProductImage,
                    int cartProductPrice, int cartQuantity) {
        this.productID = productID;
        this.cartProductName = cartProductName;
        this.cartProductImage = cartProductImage;
        this.cartProductPrice = cartProductPrice;
        this.cartQuantity = cartQuantity;
        this.selected = false;
    }

    public String getProductID() {
        return productID;
    }

    public String getCartProductName() {
        return cartProductName;
    }

    public String getCartProductImage() {
        return cartProductImage;
    }

    public int getCartProductPrice() {
        return cartProductPrice;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
