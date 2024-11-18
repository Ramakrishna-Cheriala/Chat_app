package com.chat_app.Chat_App.controller;

import com.chat_app.Chat_App.Config.jwtProvider;
import com.chat_app.Chat_App.Service.CustomerUserDetailsService;
import com.chat_app.Chat_App.Service.UserService;
import com.chat_app.Chat_App.models.User;
import com.chat_app.Chat_App.repository.UserRepository;
import com.chat_app.Chat_App.request.LoginRequest;
import com.chat_app.Chat_App.responce.AuthResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @PostMapping("/signup")
    public AuthResponce createUser(@RequestBody User user) {
//        System.out.println(user.toString());
        try {
            User isEmailExist = userRepository.findByEmail(user.getEmail());
            User isUsernameExist = userRepository.findByUsername(user.getusername());
            User isNumberExist = userRepository.findByNumber(user.getNumber());

            if (isEmailExist != null || isUsernameExist != null || isNumberExist != null) {
                throw new Exception("Email already exits");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword())); //encrypting the password

            User savedUser = userRepository.save(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
            String token = jwtProvider.generateToken(authentication);

            AuthResponce authResponce = new AuthResponce(token, "User register successfully");

            return authResponce;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public AuthResponce login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtProvider.generateToken(authentication);

        AuthResponce authResponce = new AuthResponce(token, "Login successfully");

        return authResponce;

    }


    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("User doesn't exist!");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
