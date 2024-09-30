package connectripbe.connectrip_be.chat.entity;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.global.entity.BaseEntity;
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
import org.springframework.data.annotation.CreatedDate;

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

    // 방장 설정: ChatRoomMember 의 ID를 참조
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private ChatRoomMemberEntity currentLeader;

    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;

    private String lastChatMessage;

    // 마지막 채팅 시간. 정렬을 위해 기본적으로 CreatedAt 값을 사용하고, 채팅이 발생할 때마다 업데이트
    @CreatedDate
    private LocalDateTime lastChatTime;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomMemberEntity> chatRoomMembers = new ArrayList<>();

    /**
     * ChatRoomMember 추가 및 양방향 관계 설정 메서드
     *
     * @param chatRoomMember 채팅참여자 객체
     */
    public void addChatRoomMember(ChatRoomMemberEntity chatRoomMember) {
        this.chatRoomMembers.add(chatRoomMember);
        chatRoomMember.assignChatRoom(this);
    }

    // 방장 설정 메서드 (초기 방장 설정)
    public void setInitialLeader(ChatRoomMemberEntity leader) {
        this.currentLeader = leader;
        this.addChatRoomMember(leader);
    }


    // 채팅방 상태 변경 메서드
    public void changeChatRoomType(ChatRoomType chatRoomType) {
        this.chatRoomType = chatRoomType;
    }

    //마지막 채팅 메시지 및 시간 업데이트 메서드
    public void updateLastChatMessage(String message, LocalDateTime time) {
        this.lastChatMessage = message;
        this.lastChatTime = time;
    }


}
