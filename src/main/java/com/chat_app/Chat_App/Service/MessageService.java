package com.chat_app.Chat_App.Service;


import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.responce.ChatDTO;
import com.chat_app.Chat_App.responce.MessageDTO;

import java.util.List;

public interface MessageService {

    public Chat getOrCreatePrivateChat(Integer user1_id, Integer user2_id);

    public List<MessageDTO> getAllMessages(Integer chatId);

    public String sendMessage(Integer sender, Integer receiver, String content);

    public List<ChatDTO> getAllChatsOfUser(Integer userId);

}
