package connectripbe.connectrip_be.chat.entity;

import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "chat_room")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class ChatRoomEntity extends BaseEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @OneToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "accompany_post_id")
      private AccompanyPostEntity accompanyPost;

      @Enumerated(EnumType.STRING)
      private ChatRoomType chatRoomType;

      private String lastChatMessage;

      private LocalDateTime lastChatTime;

      @Builder.Default
      @OneToMany(mappedBy = "chatRoom")
      private List<ChatRoomMemberEntity> chatRoomMembers = new ArrayList<>();

      /**
       *  ChatRoomMember 추가 및 양방향 관계 설정 메서드
       * @param chatRoomMember 채팅참여자 객체
       */
      public void addChatRoomMember(ChatRoomMemberEntity chatRoomMember) {
            this.chatRoomMembers.add(chatRoomMember);
            chatRoomMember.assignChatRoom(this);
      }

      //TODO 마지막 채팅 메시지 및 시간 업데이트 메서드


}
