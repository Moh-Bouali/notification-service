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
        // Logic to send notification
        System.out.println("Received friend request for user: " + request.requested_username());

        String destination = "/topic/requests/" + request.requested_id();
        System.out.println("Sending notification to " + destination);

        // Convert the FriendshipRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            messagingTemplate.convertAndSend(destination, requestJson);
            System.out.println(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    // Handle friendship created events
    @RabbitListener(queues = RabbitMQConfig.FRIENDSHIP_RESPONSE_QUEUE)
    public void handleFriendshipEvent(FriendshipResponse event) {
        // Logic to notify users of a new friendship
        System.out.println("User " + event.requested_username() + " and user " + event.requester_username() + " are now friends!");

        String destination = "/topic/responses/" + event.requester_id();
        // Here you would implement logic to send a notification to the users
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(event);
            messagingTemplate.convertAndSend(destination, requestJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
