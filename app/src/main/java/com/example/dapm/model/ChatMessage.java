package com.example.dapm.model;

import java.util.Date;

public class ChatMessage {
    private String senderID;      // ID của người gửi
    private String receiverID;    // ID của người nhận
    private String messageContent; // Nội dung tin nhắn
    private Date timestamp;       // Thời gian gửi

    // Constructor
    public ChatMessage() {}

    public ChatMessage(String senderID, String receiverID, String messageContent, Date timestamp) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
