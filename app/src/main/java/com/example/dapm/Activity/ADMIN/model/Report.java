package com.example.dapm.Activity.ADMIN.model;

public class Report {
    private String email;
    private boolean isReported;
    private String phoneNumber;
    private String reason;
    private String reportedUserID;
    private String reporterUserID;
    private String avatarUrl;

    // Empty constructor required for Firestore
    public Report() {}

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isReported() { return isReported; }
    public void setReported(boolean reported) { isReported = reported; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getReportedUserID() { return reportedUserID; }
    public void setReportedUserID(String reportedUserID) { this.reportedUserID = reportedUserID; }

    public String getReporterUserID() { return reporterUserID; }
    public void setReporterUserID(String reporterUserID) { this.reporterUserID = reporterUserID; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}


