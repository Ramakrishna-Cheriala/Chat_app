package com.chat_app.Chat_App.Service.Implementation;

import com.chat_app.Chat_App.Service.MessageService;
import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.models.Message;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.ChatRepository;
import com.chat_app.Chat_App.repository.MessageRepository;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.responce.ChatDTO;
import com.chat_app.Chat_App.responce.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImplementation implements MessageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @Override
    public Chat getOrCreatePrivateChat(Integer user1_id, Integer user2_id) {

        User user1 = userRepository.findById(user1_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user1_id));
        User user2 = userRepository.findById(user2_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user2_id));
        if (!user1.getFriends().contains(user2_id) || !user2.getFriends().contains(user1_id)) {
            throw new RuntimeException("The users are not friends and cannot create a private chat.");
        }


        return chatRepository.findPrivateChat(user1, user2, Chat.ChatType.PRIVATE).orElseGet(() -> {
            Chat newChat = new Chat();
            newChat.setChat_name(user2.getusername());
            newChat.setChatType(Chat.ChatType.PRIVATE);
            newChat.setTimeStamp(LocalDateTime.now());
            newChat.setUsers(new ArrayList<>(List.of(user1, user2)));
            return chatRepository.save(newChat);
        });

    }

    @Override
    public List<MessageDTO> getAllMessages(Integer chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        return chat.getMessages().stream().map(message -> new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getTimeStamp(),
                message.getSender().getId(),
                message.getReceiver().getId()
        )).collect(Collectors.toList());
    }

    @Override
    public String sendMessage(Integer senderId, Integer receiverId, String content) {
        try {
            Chat chat = getOrCreatePrivateChat(senderId, receiverId);
            User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found with Id: " + senderId));
            User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found with Id: " + receiverId));


            Message message = new Message();
            message.setChat(chat);
            message.setContent(content);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setTimeStamp(LocalDateTime.now());
            chat.setTimeStamp(LocalDateTime.now());


            chatRepository.save(chat);
            messageRepository.save(message);
            return "Message Sent";
        } catch (Exception e) {
            throw new RuntimeException("Error sending message" + e);
        }

    }

    @Override
    public List<ChatDTO> getAllChatsOfUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Chat> chats = chatRepository.findAllChatsByUser(user);

        return chats.stream().map(chat -> new ChatDTO(
                chat.getId(),
                chat.getChat_name(),
                chat.getChatType(),
                chat.getTimeStamp()
        )).collect(Collectors.toList());
    }


}
