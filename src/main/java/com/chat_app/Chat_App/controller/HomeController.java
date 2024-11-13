package com.chat_app.Chat_App.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // used for defining end points
public class HomeController {

    @GetMapping
    public String homeControllerHandler() {
        return "Hello world";
    }
}
