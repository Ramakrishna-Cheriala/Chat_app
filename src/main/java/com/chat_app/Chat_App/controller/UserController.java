package com.chat_app.Chat_App.controller;

import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.responce.UserFriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("HERE I AM");
        return userService.getAllUsers();
    }


    @GetMapping("/users/id/{id}")
    public User getUserById(@PathVariable Integer id) {
//        return userService.getUserById(id);
        return null;
    }


    @PutMapping("/users/update")
    public User updateUser(@RequestHeader("Authorization") String jwt, @RequestBody User user) {
        User getUser = userService.getProfile(jwt);
        return userService.updateUser(user, getUser.getId());
    }

    @DeleteMapping("/users/delete")
    public String deleteUser(@RequestHeader("Authorization") String jwt) {
        User getUser = userService.getProfile(jwt);
        return userService.deleteUser(getUser.getId());
    }

    @GetMapping("/users/{username}")
    public User findByEmail(@PathVariable String username) {
        return userService.getUserByUsername((username));
    }

    @GetMapping("/users/search-user/{query}")
    public List<User> findUser(@PathVariable String query) {
        return userService.searchUser(query);
    }

    @PutMapping("/users/send-request/{receiverId}")
    public String sendRequest(@RequestHeader("Authorization") String jwt, @PathVariable Integer receiverId) {
        User getUser = userService.getProfile(jwt);  // sender
        return userService.sendRequest(getUser.getId(), receiverId);
    }

    @PutMapping("/users/accept-request/{requesterId}")
    public String acceptRequest(@RequestHeader("Authorization") String jwt, @PathVariable Integer requesterId) {
        User getUser = userService.getProfile(jwt);
        return userService.acceptRequest(getUser.getId(), requesterId);
    }

    @PutMapping("/users/decline-request/{requesterId}")
    public String declineRequest(@RequestHeader("Authorization") String jwt, @PathVariable Integer requesterId) {
        User getUser = userService.getProfile(jwt);
        return userService.declineRequest(getUser.getId(), requesterId);
    }

    @GetMapping("/users/get-all-friends")
    public List<UserFriendsDTO> getAllFriends(@RequestHeader("Authorization") String jwt) {
        User getUser = userService.getProfile(jwt);
        return userService.getFriends(getUser.getId());
    }

    @GetMapping("/users/profile")
    public User getProfile(@RequestHeader("Authorization") String jwt) {
        return userService.getProfile(jwt);
    }

}
