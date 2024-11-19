package com.chat_app.Chat_App.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FriendRequest> sentFriendRequest;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FriendRequest> receivedFriendRequest;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipant> chatParticipants = new ArrayList<>();


    public User() {

    }

    public User(Integer id, String username, String email, String password, String number, List<FriendRequest> sentFriendRequest, List<FriendRequest> receivedFriendRequest, Set<User> friends, List<ChatParticipant> chatParticipants) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.number = number;
        this.sentFriendRequest = sentFriendRequest;
        this.receivedFriendRequest = receivedFriendRequest;
        this.friends = friends;
        this.chatParticipants = chatParticipants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public List<ChatParticipant> getChatParticipants() {
        return chatParticipants;
    }

    public void setChatParticipants(List<ChatParticipant> chatParticipants) {
        this.chatParticipants = chatParticipants;
    }
}