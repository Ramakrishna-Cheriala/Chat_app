package com.chat_app.Chat_App.models;

import jakarta.persistence.*;

@Entity
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private String customChatName;

    public ChatParticipant() {

    }

    public ChatParticipant(Integer id, Chat chat, User user, String customChatName) {
        this.id = id;
        this.chat = chat;
        this.user = user;
        this.customChatName = customChatName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCustomChatName() {
        return customChatName;
    }

    public void setCustomChatName(String customChatName) {
        this.customChatName = customChatName;
    }
}
