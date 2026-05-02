package com.mediconnect.chat.controller;

import com.mediconnect.chat.dto.ChatMessageDTO;
import com.mediconnect.chat.enums.MessageType;
import com.mediconnect.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // Send message to room
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDTO sendMessage(@DestinationVariable String roomId,
                                      @Payload ChatMessageDTO message) {
        chatService.saveMessage(roomId, message);
        return message;
    }

    // User joins room
    @MessageMapping("/chat/{roomId}/join")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDTO joinRoom(@DestinationVariable String roomId,
                                   @Payload ChatMessageDTO message,
                                   SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSenderName());
        message.setType(MessageType.JOIN);
        message.setContent(message.getSenderName() + " joined the consultation");
        return message;
    }
}