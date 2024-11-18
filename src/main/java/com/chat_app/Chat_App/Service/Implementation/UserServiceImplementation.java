package com.chat_app.Chat_App.Service.Implementation;

import com.chat_app.Chat_App.Config.jwtProvider;
import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.FriendRequest;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.FriendRequestRepository;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.responce.UserFriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service  // for implementing business logics
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;


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
            User senderData = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
            User receiverData = userRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));

            FriendRequest requestExists = friendRequestRepository.existsBySenderIdAndReceiverIdAndStatus(senderId, receiverId, FriendRequest.Status.PENDING);
            if (requestExists != null) {
                return "Friend request already sent";
            }

            FriendRequest friendRequest = new FriendRequest(senderId, receiverId, FriendRequest.Status.PENDING);

            friendRequestRepository.save(friendRequest);

            return "Friend request sent successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error sending friend request: " + e.getMessage());
        }
    }


    @Override
    public List<UserFriendsDTO> getFriends(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            List<Integer> friendsIds = user.getFriends();
            return userRepository.findAllById(friendsIds).stream()
                    .map(friend -> new UserFriendsDTO(friend.getusername(), friend.getNumber()))
                    .collect(Collectors.toList());
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

            FriendRequest friendRequest = friendRequestRepository.existsRequest(userId, requesterId, FriendRequest.Status.PENDING);
            if (friendRequest == null) {
                return "No pending friend request found";
            }


            friendRequest.setStatus(FriendRequest.Status.ACCEPTED);
            friendRequestRepository.save(friendRequest);


            // Update friends lists
            user.getFriends().add(requesterId);
            requester.getFriends().add(userId);

            user.getReceivedFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));
            requester.getSentFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));

            userRepository.save(user);
            userRepository.save(requester);

            return "Request accepted";
        } catch (Exception e) {
            throw new RuntimeException("Error accepting requests", e);
        }
    }

    @Override
    public String declineRequest(Integer userId, Integer requesterId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            System.out.println("Receiver: " + user.getusername());

            User requester = userRepository.findById(requesterId)
                    .orElseThrow(() -> new RuntimeException("Requester not found with ID: " + requesterId));
            System.out.println("Sender: " + requester.getusername());

            FriendRequest friendRequest = friendRequestRepository.existsRequest(userId, requesterId, FriendRequest.Status.PENDING);
            if (friendRequest == null) {
                return "No pending friend request found";
            }

            user.getReceivedFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));
            requester.getSentFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));

            friendRequestRepository.delete(friendRequest);

            userRepository.save(user);
            userRepository.save(requester);

            return "Request declined";
        } catch (Exception e) {
            throw new RuntimeException("Error declining requests", e);
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
