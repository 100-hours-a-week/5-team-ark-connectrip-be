package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomId(Long chatRoomId);
    // List<ChatMessage> findAllByChatRoomI
}
