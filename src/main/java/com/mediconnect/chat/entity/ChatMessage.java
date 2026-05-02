package com.mediconnect.chat.entity;

import com.mediconnect.chat.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;      // appointmentId as room

    private String senderEmail;

    private String senderName;

    @Column(length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @CreatedDate
    private LocalDateTime sentAt;
}
