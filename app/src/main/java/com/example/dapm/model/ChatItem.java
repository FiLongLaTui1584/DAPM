package com.example.dapm.model;

import java.util.Date;

public class ChatItem {
    private String otherUserID;
    private String otherUserName;
    private String otherUserAvatar;
    private String lastMessage;
    private Date lastMessageTimestamp;

    public ChatItem() {
    }

    public ChatItem(String otherUserID, String otherUserName, String otherUserAvatar, String lastMessage, Date lastMessageTimestamp) {
        this.otherUserID = otherUserID;
        this.otherUserName = otherUserName;
        this.otherUserAvatar = otherUserAvatar;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getOtherUserID() {
        return otherUserID;
    }

    public void setOtherUserID(String otherUserID) {
        this.otherUserID = otherUserID;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getOtherUserAvatar() {
        return otherUserAvatar;
    }

    public void setOtherUserAvatar(String otherUserAvatar) {
        this.otherUserAvatar = otherUserAvatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Date lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}

