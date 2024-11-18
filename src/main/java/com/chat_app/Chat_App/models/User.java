package com.chat_app.Chat_App.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity // to create table in the db
@Table(name = "users")  // to change the name of the table
public class User {

    @Id // to create a unique identifier in the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-generates the id
    private Integer id;
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String number;


    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> sentFriendRequest;

    @OneToMany(mappedBy = "receiverId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> receivedFriendRequest;


    private List<Integer> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats = new ArrayList<>();


    public User() {

    }

    public User(Integer id, String username, String email, String password, String number, List<FriendRequest> sentFriendRequest, List<FriendRequest> receivedFriendRequest, List<Integer> friends, List<Chat> chats) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.number = number;
        this.sentFriendRequest = sentFriendRequest;
        this.receivedFriendRequest = receivedFriendRequest;
        this.friends = friends;
        this.chats = chats;
    }

    public String getusername() {
        return username;
    }

    public List<Integer> getFriends() {
        if (friends == null) {
            friends = new ArrayList<>();
        }
        return friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<FriendRequest> getSentFriendRequest() {
        return sentFriendRequest;
    }

    public void setSentFriendRequest(List<FriendRequest> sentFriendRequest) {
        this.sentFriendRequest = sentFriendRequest;
    }

    public List<FriendRequest> getReceivedFriendRequest() {
        return receivedFriendRequest;
    }

    public void setReceivedFriendRequest(List<FriendRequest> receivedFriendRequest) {
        this.receivedFriendRequest = receivedFriendRequest;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}