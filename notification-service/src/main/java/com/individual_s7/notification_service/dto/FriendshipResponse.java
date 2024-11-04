package com.individual_s7.notification_service.dto;

public record FriendshipResponse(Long requester_id,Long requested_id, String requester_username, String requested_username) {
}
