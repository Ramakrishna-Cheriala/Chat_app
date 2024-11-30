package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    Page<Message> findByChatIdOrderByTimeStamp(Integer chatId, Pageable pageable);

    Page<Message> findByChatId(Integer chatId, Pageable pageable);


}
