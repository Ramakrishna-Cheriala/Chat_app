package com.chat_app.Chat_App.controller;

import com.chat_app.Chat_App.responce.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/users/{id}/private")
    public MessageDTO sendToUser(@Payload MessageDTO message, @DestinationVariable String id) {
        System.out.println("erripuk id: " + id);
//        simpMessagingTemplate.convertAndSendToUser(id, "/private", message);

        simpMessagingTemplate.convertAndSend("/users/" + id + "/private", message);
        return message;
    }
}
