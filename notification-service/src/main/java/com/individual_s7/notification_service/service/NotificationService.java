package com.individual_s7.notification_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.individual_s7.notification_service.configuration.RabbitMQConfig;
import com.individual_s7.notification_service.dto.FriendshipRequest;
import com.individual_s7.notification_service.dto.FriendshipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RabbitMQConfig.FRIEND_REQUEST_QUEUE)
    public void handleFriendRequest(FriendshipRequest request) {

        String destination = "/topic/requests/" + request.requested_id();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            messagingTemplate.convertAndSend(destination, requestJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_FRIENDSHIP_RESPONSE_QUEUE)
    public void handleFriendshipEvent(FriendshipResponse event) {

        String destination = "/topic/responses/" + event.requester_id();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(event);
            messagingTemplate.convertAndSend(destination, requestJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
