package com.mediconnect.call.controller;

import com.mediconnect.call.dto.SignalMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    // Send WebRTC offer
    @MessageMapping("/signal/{roomId}/offer")
    public void handleOffer(@DestinationVariable String roomId,
                            @Payload SignalMessage signal) {
        messagingTemplate.convertAndSend("/topic/signal/" + roomId + "/offer", signal);
    }

    // Send WebRTC answer
    @MessageMapping("/signal/{roomId}/answer")
    public void handleAnswer(@DestinationVariable String roomId,
                             @Payload SignalMessage signal) {
        messagingTemplate.convertAndSend("/topic/signal/" + roomId + "/answer", signal);
    }

    // ICE Candidate exchange
    @MessageMapping("/signal/{roomId}/ice")
    public void handleIceCandidate(@DestinationVariable String roomId,
                                   @Payload SignalMessage signal) {
        messagingTemplate.convertAndSend("/topic/signal/" + roomId + "/ice", signal);
    }
}