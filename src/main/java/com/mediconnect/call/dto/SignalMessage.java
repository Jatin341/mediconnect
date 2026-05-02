package com.mediconnect.call.dto;

import lombok.Data;

@Data
public class SignalMessage {
    private String type;       // offer, answer, ice-candidate
    private String sdp;        // Session Description Protocol
    private String candidate;  // ICE candidate
    private String from;
    private String to;
}
