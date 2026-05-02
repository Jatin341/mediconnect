package com.mediconnect.chat.dto;

import com.mediconnect.chat.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private String roomId;
    private String senderEmail;
    private String senderName;
    private String content;
    private MessageType type;
}