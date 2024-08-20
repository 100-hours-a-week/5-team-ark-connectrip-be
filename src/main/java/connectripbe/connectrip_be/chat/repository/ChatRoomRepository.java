package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
