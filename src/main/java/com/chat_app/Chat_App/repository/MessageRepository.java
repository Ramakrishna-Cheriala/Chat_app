package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
