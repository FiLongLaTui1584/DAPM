package com.example.dapm.model;

public class Product {
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
    private String isApproved;

    public Product() {}

    public Product(String productID, String productImage1, String productImage2, String productImage3, String title, int price, String location,
                   String productDescription, String productTinhTrang, String productBaoHanh, String productXuatXu,
                   String productHDSD, String sellerID, String isApproved) {
        this.productID = productID;
        this.productImage1 = productImage1;
        this.productImage2 = productImage2;
        this.productImage3 = productImage3;
        this.productTitle = title;
        this.productPrice = price;
        this.productLocation = location;
        this.productDescription = productDescription;
        this.productTinhTrang = productTinhTrang;
        this.productBaoHanh = productBaoHanh;
        this.productXuatXu = productXuatXu;
        this.productHDSD = productHDSD;
        this.sellerID = sellerID;
        this.isApproved= isApproved;
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

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }
}
