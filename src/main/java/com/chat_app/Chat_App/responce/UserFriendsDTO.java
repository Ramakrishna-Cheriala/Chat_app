package com.chat_app.Chat_App.responce;

public class UserFriendsDTO { // DTO - Data Transfer Object
    private String username;
    private String number;

    public UserFriendsDTO(String username, String number) {
        this.username = username;
        this.number = number;
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
}
