package com.chat_app.Chat_App.repository;

import com.chat_app.Chat_App.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);

    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE (u.username LIKE %:query% OR u.number LIKE %:query% OR u.email LIKE %:query%)")
    public List<User> findUser(@Param("query") String query);
}
