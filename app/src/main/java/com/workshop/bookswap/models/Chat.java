package com.workshop.bookswap.models;

import java.util.List;

public class Chat {
    private String chatId;
    private List<String> participants;
    private String lastMessage;
    private long timestamp;

    public Chat() {}

    public Chat(String chatId, List<String> participants, String lastMessage, long timestamp) {
        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() { return chatId; }
    public List<String> getParticipants() { return participants; }
    public String getLastMessage() { return lastMessage; }
    public long getTimestamp() { return timestamp; }

    public void setChatId(String chatId) { this.chatId = chatId; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
