package com.example.dapm.model;

public class ReportSP {
    private String productID;
    private String reportReason;
    private int userPhone;
    private String userEmail;

    public ReportSP() {}

    public ReportSP(String productID, String reportReason, int userPhone, String userEmail) {
        this.productID = productID;
        this.reportReason = reportReason;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }

    // Getter và Setter cho các thuộc tính
    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public String getReportReason() { return reportReason; }
    public void setReportReason(String reportReason) { this.reportReason = reportReason; }

    public int getUserPhone() { return userPhone; }
    public void setUserPhone(int userPhone) { this.userPhone = userPhone; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
