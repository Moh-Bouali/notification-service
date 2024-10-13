//package com.individual_s7.notification_service.handler;
//
//import com.individual_s7.notification_service.service.JwtUtil;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Mono;
//import java.net.URI;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//@Component
//public class NotificationWebSocketHandler implements WebSocketHandler {
//
//    private final JwtUtil jwtUtil;
//
//    public NotificationWebSocketHandler(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        Logger logger = LoggerFactory.getLogger(NotificationWebSocketHandler.class);
//
//        // Extract the query string from the handshake information
//        URI uri = session.getHandshakeInfo().getUri();
//        String query = uri.getQuery(); // Get the query string
//
//        // Parse the token from the query string
//        String tokenValue = extractToken(query);
//
//        if (tokenValue == null) {
//            logger.error("Token not found in query string.");
//            return session.close(); // Close if token is not found
//        }
//
//        return jwtUtil.validateToken(tokenValue)
//                .flatMap(isValid -> {
//                    if (!isValid) {
//                        logger.error("Invalid token: {}", tokenValue);
//                        return session.close(); // Close session if the token is invalid
//                    }
//                    // If valid, proceed with the WebSocket session
//                    return session.send(session.receive().doOnNext(msg -> {
//                        // Handle incoming messages
//                        logger.info("Received message: {}", msg.getPayloadAsText());
//                    }));
//                })
//                .onErrorResume(e -> {
//                    logger.error("Error during WebSocket session: {}", e.getMessage(), e);
//                    return session.close(); // Close if there's an error
//                });
//    }
//
//
//    private String extractToken(String query) {
//        // Extract the token value from the query string
//        if (query != null) {
//            String[] params = query.split("&");
//            for (String param : params) {
//                if (param.startsWith("token=")) {
//                    return param.substring(6); // Return the token value (excluding "token=")
//                }
//            }
//        }
//        return null; // Return null if the token is not found
//    }
//}
//
//
