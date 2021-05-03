package com.speproject.tripshare.repository;

import com.speproject.tripshare.model.ChatMessage;
import com.speproject.tripshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{

	@Query("select m from ChatMessage m where m.fromUser = ?1 or m.toUser = ?1 order by m.msgTimestamp")
	List<ChatMessage> findAllMessagesForUser(User user);

	@Query("select m from ChatMessage m where (m.fromUser=?1 and m.toUser=?2) or (m.fromUser=?2 and m.toUser=?1) order by m.msgTimestamp")
	List<ChatMessage> findChatMessagesBetweenUsers(User user1, User user2);

}
