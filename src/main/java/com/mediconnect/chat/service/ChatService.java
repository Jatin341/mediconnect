package com.mediconnect.chat.service;

import com.mediconnect.chat.dto.ChatMessageDTO;
import com.mediconnect.chat.entity.ChatMessage;
import com.mediconnect.chat.enums.MessageType;
import com.mediconnect.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveMessage(String roomId, ChatMessageDTO dto) {
        ChatMessage message = ChatMessage.builder()
                .roomId(roomId)
                .senderEmail(dto.getSenderEmail())
                .senderName(dto.getSenderName())
                .content(dto.getContent())
                .type(dto.getType())
                .build();
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(String roomId) {
        return chatMessageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }
}