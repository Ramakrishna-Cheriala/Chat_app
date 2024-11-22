package com.chat_app.Chat_App.controller;


import com.chat_app.Chat_App.Service.MessageService;
import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.responce.ChatDTO;
import com.chat_app.Chat_App.responce.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @PostMapping("/send-message/{id}")
    public String sendMessage(@RequestHeader("Authorization") String jwt, @PathVariable Integer id, @RequestBody String message) {
        User user = userService.getProfile(jwt);
        System.out.println("in send message controller");
        return messageService.sendMessage(user.getId(), id, message);
    }

    @GetMapping("/get-messages/{id}")
    public List<MessageDTO> getAllMessages(@PathVariable Integer id) {
        return messageService.getAllMessages(id);
    }

    @GetMapping("/get-chats")
    public List<ChatDTO> getAllChatsOfUser(@RequestHeader("Authorization") String jwt) {
        User user = userService.getProfile(jwt);
        return messageService.getAllChatsOfUser(user.getId());
    }

    @GetMapping("/get-chat/{id}")
    public ChatDTO getChatDetails(@RequestHeader("Authorization") String jwt, @PathVariable Integer id) {
        User user = userService.getProfile(jwt);
        return messageService.getChatDetails(user.getId(), id);
    }
}
