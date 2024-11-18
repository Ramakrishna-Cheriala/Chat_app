package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.chatType = :type AND :user1 MEMBER OF c.users AND :user2 MEMBER OF c.users")
    Optional<Chat> findPrivateChat(User user1, User user2, Chat.ChatType type);

    @Query("SELECT c FROM Chat c WHERE :user MEMBER OF c.users")
    List<Chat> findAllChatsByUser(@Param("user") User user);
}
