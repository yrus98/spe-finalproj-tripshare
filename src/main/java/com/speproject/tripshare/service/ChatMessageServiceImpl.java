package com.speproject.tripshare.service;

import com.speproject.tripshare.model.ChatMessage;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.ChatMessageRepository;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.web.dto.ChatMessageDto;
import com.speproject.tripshare.web.dto.UserProfileDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ChatMessageServiceImpl implements ChatMessageService{

    private ChatMessageRepository chatMessageRepository;
    private UserRepository userRepository;

    private ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository){
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }


    @Override
    public ChatMessage save(ChatMessageDto chatMessageDto) throws Exception {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User creatingUser = userRepository.findByEmail(userEmail);
        User toUser = userRepository.findById(chatMessageDto.getToUserId()).orElse(null);
        if(toUser==null)
            throw new Exception();
        ChatMessage chatMessage = new ChatMessage(creatingUser, toUser, chatMessageDto.getMessage(), new Timestamp(new java.util.Date().getTime()));

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<UserProfileDto> getOngoingChatUsersList(User creatingUser){
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllMessagesForUser(creatingUser);
        Set<UserProfileDto> chatUserSet = new HashSet<>();
        for (ChatMessage m :
                chatMessageList) {
            if(m.getFromUser().getId().equals(creatingUser.getId()))
                chatUserSet.add(new UserProfileDto(m.getToUser()));
            else
                chatUserSet.add(new UserProfileDto(m.getFromUser()));
        }
        return new ArrayList<>(chatUserSet);
    }

    @Override
    public List<ChatMessage> getChatsBetweenUsers(User user1, User user2){
        List<ChatMessage> chatMessageList = chatMessageRepository.findChatMessagesBetweenUsers(user1, user2);
        for (ChatMessage m :
                chatMessageList) {
            m.getFromUser().setPassword("");
            m.getToUser().setPassword("");
            m.getFromUser().setRole(null);
            m.getToUser().setRole(null);
        }
        return chatMessageList;
    }
}
