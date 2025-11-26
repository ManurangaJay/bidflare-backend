package com.bidflare.backend.config;

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
        // The endpoint the client connects to
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Configure CORS correctly for production
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix for messages sent from server to client
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefix for messages sent from client to server (if needed)
        registry.setApplicationDestinationPrefixes("/app");
    }
}