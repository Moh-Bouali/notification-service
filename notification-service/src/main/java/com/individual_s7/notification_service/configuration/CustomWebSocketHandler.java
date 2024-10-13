//package com.individual_s7.notification_service.configuration;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomWebSocketHandler implements WebSocketHandler {
//
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        return session.send(
//                session.receive()
//                        .map(msg -> session.textMessage("Received: " + msg.getPayloadAsText()))  // Echo the received messages
//        );
//    }
//}
