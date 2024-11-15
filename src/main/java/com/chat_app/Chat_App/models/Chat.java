package com.chat_app.Chat_App.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String Chat_name;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    private LocalDateTime timeStamp;
}
