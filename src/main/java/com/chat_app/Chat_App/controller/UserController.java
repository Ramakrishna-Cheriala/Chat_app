package com.chat_app.Chat_App.controller;

import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        System.out.println("HERE I AM");
        return userService.getAllUsers();
    }


    @GetMapping("/api/users/id/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PutMapping("/api/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Integer id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/api/users/{id}")
    public Boolean deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/api/users/{username}")
    public User findByEmail(@PathVariable String username) {
        return userService.getUserByUsername((username));
    }

    @GetMapping("/api/users/search-user/{query}")
    public List<User> findUser(@PathVariable String query) {
        return userService.searchUser(query);
    }

    @PutMapping("/api/users/send-request/{receiverId}")
    public String sendRequest(@PathVariable Integer receiverId) {
        int senderId = 4;
        return userService.sendRequest(senderId, receiverId);
    }

    @PutMapping("/api/users/accept-request/{requesterId}")
    public String acceptRequest(@PathVariable Integer requesterId) {
        int userId = 3;
        return userService.acceptRequest(userId, requesterId);
    }

    @GetMapping("/api/users/get-all-friends/{userId}")
    public List<User> getAllFriends(@PathVariable Integer userId) {
        return userService.getFriends(userId);
    }

}
