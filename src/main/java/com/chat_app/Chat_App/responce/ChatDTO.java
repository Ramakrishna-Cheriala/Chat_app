package com.chat_app.Chat_App.responce;

import com.chat_app.Chat_App.models.Chat;

import java.time.LocalDateTime;

public class ChatDTO {
    private Integer id;
    private String chatName;
    private Integer userId;
    private Chat.ChatType chatType;
    private LocalDateTime timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Chat.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(Chat.ChatType chatType) {
        this.chatType = chatType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ChatDTO(Integer id, String chatName, Integer userId, Chat.ChatType chatType, LocalDateTime timeStamp) {
        this.id = id;
        this.chatName = chatName;
        this.userId = userId;
        this.chatType = chatType;
        this.timeStamp = timeStamp;
    }
}
