package com.chat_app.Chat_App.Service;

import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.responce.UserFriendsDTO;

import java.util.List;

public interface UserService {

    public User registerUser(User user);

    public User getUserById(Integer id);

    public User getUserByUsername(String username);

    // todo fix the function after implementing login function
    public String sendRequest(Integer senderId, Integer receiverId);

    public List<UserFriendsDTO> getFriends(Integer id);

    public String acceptRequest(Integer userId, Integer requesterId);

    public String declineRequest(Integer userId, Integer requesterId);

    public User updateUser(User user, Integer id);

    public List<User> searchUser(String query);

    public List<User> getAllUsers();

    public String deleteUser(Integer id);

    public User getProfile(String jwt);

    public List<UserFriendsDTO> getSearchFriends(Integer id, String query);
}
