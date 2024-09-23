package connectripbe.connectrip_be.chat.repository;

import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    Optional<ChatRoomEntity> findByAccompanyPost_Id(long id);


    @Query("select cr from chat_room cr "
            + "join fetch cr.accompanyPost where cr.id =: chatRoomId")
    Optional<ChatRoomEntity> findByIdWithPost(@Param("chatRoomId") Long chatRoomId);
}
