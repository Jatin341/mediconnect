package com.mediconnect.notification.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, String message, String type) {
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + userId,
                new NotificationDTO(message, type, System.currentTimeMillis())
        );
    }

    @Data
    @AllArgsConstructor
    public static class NotificationDTO {
        private String message;
        private String type;
        private long timestamp;
    }
}