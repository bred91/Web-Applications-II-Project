
package it.polito.server.tickets.messages

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig:WebSocketMessageBrokerConfigurer {


    override fun registerStompEndpoints(registry:StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3006").withSockJS();
    }


    override fun configureMessageBroker(registry:MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topics");
        registry.setUserDestinationPrefix("/topics");
    }
}