package com.example.dapm.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productID;
    private String productImage1;
    private String productImage2;
    private String productImage3;
    private String productTitle;
    private int productPrice;
    private String productLocation;
    private String productDescription;
    private String productTinhTrang;
    private String productBaoHanh;
    private String productXuatXu;
    private String productHDSD;
    private String sellerID;
    private String categoryID;
    private String isApproved;
    private int productQuantity;


    private boolean isFavorite;

    public Product() {}

    public Product(String productID, String productImage1, String productImage2, String productImage3, String productTitle, int productPrice, String productLocation, String productDescription, String productTinhTrang, String productBaoHanh, String productXuatXu, String productHDSD, String sellerID, String categoryID, String isApproved, int productQuantity) {
        this.productID = productID;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productLocation = productLocation;
        this.productDescription = productDescription;
        this.productTinhTrang = productTinhTrang;
        this.productBaoHanh = productBaoHanh;
        this.productXuatXu = productXuatXu;
        this.productHDSD = productHDSD;
        this.sellerID = sellerID;
        this.categoryID = categoryID;
        this.isApproved = isApproved;
        this.productQuantity = productQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage1() {
        return productImage1;
    }

    public void setProductImage1(String productImage1) {
        this.productImage1 = productImage1;
    }

    public String getProductImage2() {
        return productImage2;
    }

    public void setProductImage2(String productImage2) {
        this.productImage2 = productImage2;
    }

    public String getProductImage3() {
        return productImage3;
    }

    public void setProductImage3(String productImage3) {
        this.productImage3 = productImage3;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductTinhTrang() {
        return productTinhTrang;
    }

    public void setProductTinhTrang(String productTinhTrang) {
        this.productTinhTrang = productTinhTrang;
    }

    public String getProductBaoHanh() {
        return productBaoHanh;
    }

    public void setProductBaoHanh(String productBaoHanh) {
        this.productBaoHanh = productBaoHanh;
    }

    public String getProductXuatXu() {
        return productXuatXu;
    }

    public void setProductXuatXu(String productXuatXu) {
        this.productXuatXu = productXuatXu;
    }

    public String getProductHDSD() {
        return productHDSD;
    }

    public void setProductHDSD(String productHDSD) {
        this.productHDSD = productHDSD;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }




    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
