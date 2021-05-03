package com.speproject.tripshare.service;

import com.speproject.tripshare.model.ChatMessage;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.web.dto.ChatMessageDto;
import com.speproject.tripshare.web.dto.UserProfileDto;

import java.util.List;

public interface ChatMessageService {
	ChatMessage save(ChatMessageDto chatMessageDto) throws Exception;

	List<UserProfileDto> getOngoingChatUsersList(User creatingUser);

	List<ChatMessage> getChatsBetweenUsers(User user1, User user2);
}
