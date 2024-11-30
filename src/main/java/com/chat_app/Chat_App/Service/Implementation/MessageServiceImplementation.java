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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
    public ChatDTO getPrivateChat(Integer user1_id, Integer user2_id) {

        User user1 = userRepository.findById(user1_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user1_id));
        User user2 = userRepository.findById(user2_id).orElseThrow(() -> new RuntimeException("User not found with Id: " + user2_id));
        if (!user1.getFriends().contains(user2) || !user2.getFriends().contains(user1)) {
            throw new RuntimeException("The users are not friends and cannot create a private chat.");
        }

        Chat chat = chatRepository.findPrivateChat(user1, user2, Chat.ChatType.PRIVATE).orElseThrow(() -> new RuntimeException("No chat found"));

        User otherParticipant = chat.getParticipants().stream()
                .map(ChatParticipant::getUser) // Get the user of each participant
                .filter(participant -> !participant.equals(user1)) // Exclude the current user
                .findFirst() // Get the first match (for 1-to-1 chats)
                .orElse(null); // Handle cases with no other participants

        String otherParticipantName = otherParticipant != null ? otherParticipant.getusername() : "Unknown";
        Integer otherParticipantId = otherParticipant != null ? otherParticipant.getId() : null;
        return new ChatDTO(
                chat.getId(),
                otherParticipantName,
                otherParticipantId,
                chat.getChatType(),
                chat.getTimeStamp()
        );

    }


    @Override
    public List<MessageDTO> getAllMessages(Integer chatId, int page, int size) {
        // Define pageable to fetch the most recent messages
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));

        // Fetch messages in descending order
        Page<Message> messagePage = messageRepository.findByChatId(chatId, pageable);

        // Reverse the result to show messages in chronological order
        List<MessageDTO> recentMessages = messagePage.stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getContent(),
                        message.getTimeStamp(),
                        message.getSender().getId(),
                        message.getReceiver().getId()
                ))
                .collect(Collectors.toList());

        // Reverse the list for chronological order
        Collections.reverse(recentMessages);

        return recentMessages;
    }


    @Override
    public MessageDTO sendMessage(Integer senderId, Integer receiverId, String content) {
        try {
            User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found with Id: " + senderId));
            User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found with Id: " + receiverId));
            Chat chat = chatRepository.findPrivateChat(sender, receiver, Chat.ChatType.PRIVATE).orElseThrow(() -> new RuntimeException("No chat found"));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
            String compactContent = objectMapper.writeValueAsString(objectMapper.readTree(content));

            Message message = new Message();
            message.setChat(chat);
            message.setContent(compactContent);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setTimeStamp(LocalDateTime.now());
            chat.setTimeStamp(LocalDateTime.now());


            chatRepository.save(chat);
            messageRepository.save(message);


            return new MessageDTO(
                    message.getId(),
                    compactContent,
                    message.getTimeStamp(),
                    sender.getId(),
                    receiver.getId()
            );
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
                    User otherParticipant = chat.getParticipants().stream()
                            .map(ChatParticipant::getUser) // Get the user of each participant
                            .filter(participant -> !participant.equals(user)) // Exclude the current user
                            .findFirst() // Get the first match (for 1-to-1 chats)
                            .orElse(null); // Handle cases with no other participants

                    String otherParticipantName = otherParticipant != null ? otherParticipant.getusername() : "Unknown";
                    Integer otherParticipantId = otherParticipant != null ? otherParticipant.getId() : null;


                    return new ChatDTO(
                            chat.getId(),
                            otherParticipantName,  // The name of the other participant
                            otherParticipantId,
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
        User otherParticipant = chat.getParticipants().stream()
                .map(ChatParticipant::getUser) // Get the user of each participant
                .filter(participant -> !participant.equals(user)) // Exclude the current user
                .findFirst() // Get the first match (for 1-to-1 chats)
                .orElse(null); // Handle cases with no other participants

        String otherParticipantName = otherParticipant != null ? otherParticipant.getusername() : "Unknown";
        Integer otherParticipantId = otherParticipant != null ? otherParticipant.getId() : null;
        return new ChatDTO(
                chat.getId(),
                otherParticipantName,
                otherParticipantId,
                chat.getChatType(),
                chat.getTimeStamp()
        );
    }


}
