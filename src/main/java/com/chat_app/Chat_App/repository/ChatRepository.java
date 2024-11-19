package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    @Query("SELECT c FROM Chat c " +
            "JOIN c.participants cp1 " +
            "JOIN c.participants cp2 " +
            "WHERE c.chatType = :type " +
            "AND cp1.user = :user1 " +
            "AND cp2.user = :user2")
    Optional<Chat> findPrivateChat(@Param("user1") User user1,
                                   @Param("user2") User user2,
                                   @Param("type") Chat.ChatType type);


}
