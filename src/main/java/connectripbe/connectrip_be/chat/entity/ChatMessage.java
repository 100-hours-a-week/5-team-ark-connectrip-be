package connectripbe.connectrip_be.chat.entity;

import connectripbe.connectrip_be.chat.entity.type.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "chat_message")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    /**
     * 몽고디비 설정해서 연결 확인 1. application.properties에 몽고디비 설정 추가 2. MognoDBConfig 클래스 생성
     */
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Field("chat_room_id")
    private Long chatRoomId;

    @Field("sender_id")
    private Long senderId;

    @Field("sender_nickname")
    private String senderNickname;

    @Field("sender_profile_image")
    private String senderProfileImage;

    @Field("content")
    private String content;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;


}
