package com.chat_app.Chat_App.Service.Implementation;

import com.chat_app.Chat_App.Service.MessageService;
import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.models.ChatParticipant;
import com.chat_app.Chat_App.models.Message;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.ChatParticipantRepository;
import com.chat_app.Chat_App.repository.ChatRepository;
import com.chat_app.Chat_App.repository.MessageRepository;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.responce.ChatDTO;
import com.chat_app.Chat_App.responce.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    ChatParticipantRepository chatParticipantRepository;

    @Override
    public Chat getPrivateChat(Integer user1_id, Integer user2_id) {

        User user1 = userRepository.findById(user1_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user1_id));
        User user2 = userRepository.findById(user2_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user2_id));
        if (!user1.getFriends().contains(user2) || !user2.getFriends().contains(user1)) {
            throw new RuntimeException("The users are not friends and cannot create a private chat.");
        }


        return chatRepository.findPrivateChat(user1, user2, Chat.ChatType.PRIVATE).orElseThrow(() -> new RuntimeException("No chat found"));

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
            Chat chat = getPrivateChat(senderId, receiverId);
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all ChatParticipants for the user to get personalized chat names
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByUser(user);


        return chatParticipants.stream()
                .map(participant -> participant.getChat())
                .filter(chat -> !chat.getMessages().isEmpty())  // Only keep chats with at least one message
                .map(chat -> {
                    // Find the other participant's custom name
                    String otherParticipantName = chat.getParticipants().stream()
                            .filter(p -> p.getUser().equals(user)) // Find the participant that is not the current user
                            .findFirst()
                            .map(ChatParticipant::getCustomChatName)  // Get the custom chat name
                            .orElse("Unknown");  // Default name if the other participant is not found

                    return new ChatDTO(
                            chat.getId(),
                            otherParticipantName,  // The name of the other participant
                            chat.getChatType(),
                            chat.getTimeStamp()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChatDTO getChatDetails(Integer id, Integer chatId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        String otherParticipantName = chat.getParticipants().stream()
                .filter(p -> p.getUser().equals(user)) // Find the participant that is not the current user
                .findFirst()
                .map(ChatParticipant::getCustomChatName)  // Get the custom chat name
                .orElse("Unknown");
        return new ChatDTO(
                chat.getId(),
                otherParticipantName,  // The name of the other participant
                chat.getChatType(),
                chat.getTimeStamp()
        );
    }


}
