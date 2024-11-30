package com.chat_app.Chat_App.Service;


import com.chat_app.Chat_App.responce.ChatDTO;
import com.chat_app.Chat_App.responce.MessageDTO;

import java.util.List;

public interface MessageService {

    public ChatDTO getPrivateChat(Integer user1_id, Integer user2_id);

    List<MessageDTO> getAllMessages(Integer chatId, int page, int size);

    public MessageDTO sendMessage(Integer sender, Integer receiver, String content);

    public List<ChatDTO> getAllChatsOfUser(Integer userId);

    public ChatDTO getChatDetails(Integer id, Integer chatId);

}
