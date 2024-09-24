package connectripbe.connectrip_be.chat.entity;

import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.global.entity.BaseEntity;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "chat_room_member")
@Table(name = "chat_room_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    private ChatRoomMemberStatus status;

    @Column(name = "is_location_sharing_enabled")
    private boolean isLocationSharingEnabled;

    @Column(name = "last_latitude")
    private Double lastLatitude;

    @Column(name = "last_longitude")
    private Double lastLongitude;

    @Builder.Default
    @Column
    private String lastReadMessageId = null;

    // 연관관계 메서드
    public void assignChatRoom(ChatRoomEntity chatRoomEntity) {
        this.chatRoom = chatRoomEntity;
    }

    public void updateStatus(ChatRoomMemberStatus status) {
        this.status = status;
    }

    public void exitChatRoom() {
        this.status = ChatRoomMemberStatus.EXIT;
    }

    public void activeChatRoom() {
        this.status = ChatRoomMemberStatus.ACTIVE;
    }

    public boolean isLocationSharingEnabled() {
        return isLocationSharingEnabled;
    }

    public void enableLocationSharing() {
        isLocationSharingEnabled = true;
    }

    public void disableLocationSharing() {
        isLocationSharingEnabled = false;
    }

    public void updateLocation(Double latitude, Double longitude) {
        this.lastLatitude = latitude;
        this.lastLongitude = longitude;
    }

    public void updateLastReadMessageId(String lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
