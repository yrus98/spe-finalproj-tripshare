package com.speproject.tripshare.web.dto;

public class ChatMessageDto {

    private Long toUserId;

    private String message;

    public ChatMessageDto(){

    }

    public ChatMessageDto(Long toUserId, String message) {
        super();
        this.toUserId = toUserId;
        this.message = message;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
