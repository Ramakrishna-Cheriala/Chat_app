package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.FriendRequest;
import com.chat_app.Chat_App.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    @Query("SELECT r FROM FriendRequest r WHERE r.sender = :sender AND r.receiver = :receiver AND r.status = :status")
    FriendRequest findBySenderAndReceiverAndStatus(@Param("sender") User sender, @Param("receiver") User receiver, @Param("status") FriendRequest.Status status);


}
