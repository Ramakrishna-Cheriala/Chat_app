package com.chat_app.Chat_App.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-ws")
                .setAllowedOriginPatterns("*") // Replace with specific domains in production
                .withSockJS(); // Enables fallback options for older browsers
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for client-sent messages
        registry.enableSimpleBroker("/chat", "/users"); // Topics for broadcasting messages
        registry.setUserDestinationPrefix("/users"); // Prefix for private messages
    }
}

