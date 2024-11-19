package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.ChatParticipant;
import com.chat_app.Chat_App.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Integer> {

    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.user = :user")
    List<ChatParticipant> findAllByUser(@Param("user") User user);

}
