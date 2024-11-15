package com.chat_app.Chat_App.Service.Implementation;

import com.chat_app.Chat_App.Config.jwtProvider;
import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // for implementing business logics
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public User registerUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    @Override
    public User getUserById(Integer id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by ID: " + e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by username: " + e.getMessage());
        }
    }

    @Override
    public String sendRequest(Integer senderId, Integer receiverId) {
        try {
            User receiverData = userRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            List<Integer> requests = receiverData.getRequests();
            if (!requests.contains(senderId)) {
                requests.add(senderId);
            }

            userRepository.save(receiverData);
            return "Request sent successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error sending friend request: " + e.getMessage());
        }
    }


    @Override
    public List<User> getFriends(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            List<Integer> friendsIds = user.getFriends();

            List<User> friends = userRepository.findAllById(friendsIds);

            return friends;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving friends", e);
        }
    }

    @Override
    public String acceptRequest(Integer userId, Integer requesterId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            User requester = userRepository.findById(requesterId)
                    .orElseThrow(() -> new RuntimeException("Requester not found with ID: " + requesterId));

            // Check if the request is present
            List<Integer> requests = user.getRequests();
            if (!requests.contains(requesterId)) {
                return "Request not found";
            }

            // Add friends and remove the request
            user.getFriends().add(requesterId);
            requester.getFriends().add(userId);
            requests.remove(requesterId); // Remove the requesterId from the user's requests

            // Save changes to the repository
            userRepository.save(user);
            userRepository.save(requester);

            return "Request accepted";
        } catch (Exception e) {
            throw new RuntimeException("Error accepting requests", e);
        }
    }

    @Override
    public User updateUser(User user, Integer id) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setusername(user.getusername());
            existingUser.setEmail(user.getEmail());
            return userRepository.save(existingUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }


    @Override
    public List<User> searchUser(String query) {
        try {
            if (query != null && query.length() > 2) {
                return userRepository.findUser(query);
            } else {
                throw new IllegalArgumentException("At least 3 characters are required for username or phone number.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error searching for user: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users: " + e.getMessage());
        }
    }

    @Override
    public String deleteUser(Integer id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return "User deleted successfully";
            } else {
                return "Error Deleting user";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public User getProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        System.out.println("email: " + email);
        return userRepository.findByEmail(email);
    }
}
