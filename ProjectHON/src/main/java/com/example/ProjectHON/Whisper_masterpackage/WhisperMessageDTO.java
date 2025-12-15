package com.example.ProjectHON.Whisper_masterpackage;

import java.time.LocalDateTime;

public class WhisperMessageDTO {

    private Long senderId;
    private Long receiverId;
    private String content;
    private String imageBase64;  // image encoded as Base64
    private Long whisperId;
    private LocalDateTime sentAt;
    private Boolean read;

    public WhisperMessageDTO() {}

    public WhisperMessageDTO(Long senderId, Long receiverId, String content, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public WhisperMessageDTO(Long senderId, Long receiverId, String content, String imageBase64, Long whisperId, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.whisperId = whisperId;
        this.sentAt = sentAt;
    }

    public WhisperMessageDTO(Long senderId, Long receiverId, String content, String imageBase64, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.sentAt = sentAt;
    }

    public WhisperMessageDTO(Long senderId, Long receiverId, String content, String imageBase64, LocalDateTime sentAt, Long whisperId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.sentAt = sentAt;
        this.whisperId = whisperId;
    }

    public WhisperMessageDTO(Long senderId, Long receiverId, String content, String imageBase64, LocalDateTime sentAt, Boolean read) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.imageBase64 = imageBase64;
        this.sentAt = sentAt;
        this.read = read;
    }

    public WhisperMessageDTO(Long senderId, Long receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Long getWhisperId() {
        return whisperId;
    }

    public void setWhisperId(Long whisperId) {
        this.whisperId = whisperId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

