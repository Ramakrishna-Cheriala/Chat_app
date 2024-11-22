package com.chat_app.Chat_App.responce;

public class UserFriendsDTO { // DTO - Data Transfer Object
    private Integer id;
    private String username;
    private String number;
    private Integer chatId;

    public UserFriendsDTO(Integer id, String username, String number, Integer chatId) {
        this.id = id;
        this.username = username;
        this.number = number;
        this.chatId = chatId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
