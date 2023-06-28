//package it.polito.server.tickets.messages
//
//import ch.qos.logback.classic.pattern.MessageConverter
//import org.springframework.context.annotation.Configuration
//import org.springframework.messaging.simp.config.MessageBrokerRegistry
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
//
//
//@Configuration
//@EnableWebSocketMessageBroker
//class WebSocketConfig : WebSocketMessageBrokerConfigurer {
//    override fun configureMessageBroker(config: MessageBrokerRegistry) {
//        config.enableSimpleBroker("/chat")
//        config.setApplicationDestinationPrefixes("/app")
//    }
//
//    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
//        registry
//            .addEndpoint("/ws")
//            .setAllowedOrigins("*")
//            .withSockJS()
//    }
//
//    /*fun configureMessageConverters(messageConverters: MutableList<MessageConverter?>): Boolean {
//        val resolver = DefaultContentTypeResolver()
//        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON)
//        val converter = MappingJackson2MessageConverter()
//        converter.setObjectMapper(ObjectMapper())
//        converter.setContentTypeResolver(resolver)
//        messageConverters.add(converter)
//        return false
//    }*/
//}