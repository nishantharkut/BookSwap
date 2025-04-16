package com.workshop.bookswap.models;

public class ChatUser {
    private String uid;
    private String username;
    private String lastMessage;
    private long timestamp; // Changed time to timestamp
    private String profileUrl; // Optional for profile pic later

    public ChatUser() {}

    public ChatUser(String uid, String username, String lastMessage, long timestamp, String profileUrl) {
        this.uid = uid;
        this.username = username;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.profileUrl = profileUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
