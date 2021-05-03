package com.speproject.tripshare.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name =  "chats", uniqueConstraints = @UniqueConstraint(columnNames = "chatId"))
public class ChatMessage {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name="fromUserId", referencedColumnName = "id")
    @JsonIgnoreProperties("tripList")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name="toUserId", referencedColumnName = "id")
    @JsonIgnoreProperties("tripList")
    private User toUser;

    public String message;

    private Timestamp msgTimestamp;

    public ChatMessage(){

    }

    public ChatMessage(User fromUser, User toUser, String message, Timestamp msgTimestamp) {
        super();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.msgTimestamp = msgTimestamp;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getMsgTimestamp() {
        return msgTimestamp;
    }

    public void setMsgTimestamp(Timestamp msgTimestamp) {
        this.msgTimestamp = msgTimestamp;
    }
}
