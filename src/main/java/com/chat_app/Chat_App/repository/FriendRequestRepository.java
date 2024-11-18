package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    @Query("SELECT r FROM FriendRequest r WHERE r.senderId = :senderId AND r.receiverId = :receiverId AND r.status = :status")
    public FriendRequest existsBySenderIdAndReceiverIdAndStatus(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId, @Param("status") FriendRequest.Status status);

    @Query("SELECT r FROM FriendRequest r WHERE r.senderId = :requesterId AND r.receiverId = :userId AND r.status = :status")
    public FriendRequest existsRequest(@Param("userId") Integer userId, @Param("requesterId") Integer requesterId, @Param("status") FriendRequest.Status status);


}
