package com.chat_app.Chat_App.Service.Implementation;

import com.chat_app.Chat_App.Config.jwtProvider;
import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.Chat;
import com.chat_app.Chat_App.models.ChatParticipant;
import com.chat_app.Chat_App.models.FriendRequest;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.ChatRepository;
import com.chat_app.Chat_App.repository.FriendRequestRepository;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.responce.UserFriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service  // for implementing business logics
public class UserServiceImplementation implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    ChatRepository chatRepository;


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

            FriendRequest requestExists = friendRequestRepository.findBySenderAndReceiverAndStatus(senderData, receiverData, FriendRequest.Status.PENDING);
            if (requestExists != null) {
                return "Friend request already sent";
            }

            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setSender(senderData);
            friendRequest.setReceiver(receiverData);
            friendRequest.setStatus(FriendRequest.Status.PENDING);

            friendRequestRepository.save(friendRequest);

            return "Friend request sent successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error sending friend request: " + e.getMessage());
        }
    }


    @Override
    public List<UserFriendsDTO> getFriends(Integer userId) {
        try {
            // Retrieve the user from the repository
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Set<User> friends = user.getFriends();


            return friends.stream()
                    .map(friend -> {
                        Chat privateChat = chatRepository
                                .findPrivateChat(user, friend, Chat.ChatType.PRIVATE)
                                .orElse(null);
                        Integer chatId = privateChat != null ? privateChat.getId() : null;
                        return new UserFriendsDTO(friend.getId(), friend.getusername(), friend.getNumber(), chatId);
                    }) // Ensure you use the correct getter names
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

            FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(requester, user, FriendRequest.Status.PENDING);
            if (friendRequest == null) {
                return "No pending friend request found";
            }


            friendRequest.setStatus(FriendRequest.Status.ACCEPTED);
            friendRequestRepository.save(friendRequest);


            // Update friends lists
            user.getFriends().add(requester);
            requester.getFriends().add(user);

            user.getReceivedFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));
            requester.getSentFriendRequest().removeIf(req -> req.getId().equals(friendRequest.getId()));

            userRepository.save(user);
            userRepository.save(requester);

            createPrivateChat(user, requester);

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

            FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiverAndStatus(user, requester, FriendRequest.Status.PENDING);
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
//        System.out.println("email: " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserFriendsDTO> getSearchFriends(Integer id, String query) {
        if (query.length() > 2) {
            // Fetch the user
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get the user's friends
            Set<User> friends = user.getFriends();

            // Filter friends based on the search query (case-insensitive)
            List<UserFriendsDTO> filteredFriends = friends.stream()
                    .filter(friend -> friend.getusername().toLowerCase().contains(query.toLowerCase()))
                    .map(friend -> {
                        // Fetch the private chat ID if it exists
                        Chat privateChat = chatRepository
                                .findPrivateChat(user, friend, Chat.ChatType.PRIVATE)
                                .orElse(null);
                        Integer chatId = privateChat != null ? privateChat.getId() : null;

                        // Map friend to UserFriendsDTO
                        return new UserFriendsDTO(
                                friend.getId(),
                                friend.getusername(),
                                friend.getEmail(),
                                chatId
                        );
                    })
                    .collect(Collectors.toList());

            return filteredFriends;
        }

        return List.of(); // Return an empty list if the query is too short
    }


    private Chat createPrivateChat(User user1, User user2) {
        // Check if a private chat already exists
        Optional<Chat> existingChat = chatRepository.findPrivateChat(user1, user2, Chat.ChatType.PRIVATE);
        if (existingChat.isPresent()) {
            return existingChat.get(); // Return existing chat if found
        }

        // Create a new private chat
        Chat newChat = new Chat();
        newChat.setChatType(Chat.ChatType.PRIVATE);
        newChat.setTimeStamp(LocalDateTime.now());

        // Add participants with custom names
        ChatParticipant participant1 = new ChatParticipant();
        participant1.setUser(user1);
        participant1.setChat(newChat);
        participant1.setCustomChatName(user2.getusername()); // User1 sees User2's name

        ChatParticipant participant2 = new ChatParticipant();
        participant2.setUser(user2);
        participant2.setChat(newChat);
        participant2.setCustomChatName(user1.getusername()); // User2 sees User1's name

        newChat.setParticipants(List.of(participant1, participant2));

        System.out.println("creating chat-------------");
        Chat savedChat = chatRepository.save(newChat);
        System.out.println("chat created successfully");

        // Save the new chat
        return savedChat;
    }
}
