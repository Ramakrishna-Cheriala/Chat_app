package com.chat_app.Chat_App.responce;

public class AuthResponce {

    private String token;
    private String message;

    public AuthResponce() {

    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthResponce(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
