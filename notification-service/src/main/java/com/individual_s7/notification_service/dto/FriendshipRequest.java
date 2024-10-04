package com.individual_s7.notification_service.dto;

import java.sql.Timestamp;

public record FriendshipRequest(Long id, Long requester_id, String requester_username,
                                       Long requested_id, String requested_username,
                                       String status, Timestamp created_at){}
