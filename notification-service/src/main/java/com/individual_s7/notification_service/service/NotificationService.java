package com.individual_s7.notification_service.service;

import com.individual_s7.notification_service.configuration.RabbitMQConfig;
import com.individual_s7.notification_service.dto.FriendshipRequest;
import com.individual_s7.notification_service.dto.FriendshipResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @RabbitListener(queues = RabbitMQConfig.FRIEND_REQUEST_QUEUE)
    public void handleFriendRequest(FriendshipRequest request) {
        // Logic to send notification
        System.out.println("Received friend request for user: " + request.requested_username());

        // Here you'd implement logic to send a WebSocket or other real-time notification to the user
    }

    // Handle friendship created events
    @RabbitListener(queues = RabbitMQConfig.FRIENDSHIP_RESPONSE_QUEUE)
    public void handleFriendshipEvent(FriendshipResponse event) {
        // Logic to notify users of a new friendship
        System.out.println("User " + event.requested_id() + " and user " + event.requester_id() + " are now friends!");

        // Here you would implement logic to send a notification to the users
    }
}
