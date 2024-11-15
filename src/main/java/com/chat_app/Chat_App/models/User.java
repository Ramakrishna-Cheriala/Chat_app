package com.chat_app.Chat_App.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    private String number;

    private List<Integer> friends = new ArrayList<Integer>();

    private List<Integer> requests = new ArrayList<Integer>();


    public User() {

    }

    public User(Integer id, String username, String email, String password, String number, List<Integer> friends, List<Integer> requests) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.number = number;
        this.friends = friends != null ? friends : new ArrayList<>();
        this.requests = requests != null ? requests : new ArrayList<>();
    }

    public String getusername() {
        return username;
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


    public List<Integer> getFriends() {
        return friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }

    public List<Integer> getRequests() {
        return requests;
    }

    public void setRequests(List<Integer> requests) {
        this.requests = requests;
    }
}
