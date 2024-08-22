package connectripbe.connectrip_be.chat.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.QChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.QChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.chat.repository.CustomChatRoomMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CustomChatRoomMemberRepositoryImpl implements CustomChatRoomMemberRepository {

    private final JPAQueryFactory jpa;

    /**
     *
     * 주어진 이메일에 해당하는 사용자가 속한 채팅방 목록을 반환
     * 채팅방 목록은 사용자가 참여 중인 방으로 제한되며,
     * 삭제되었거나 사용자가 나간 방은 제외.
     * 결과는 마지막 채팅 시간(lastChatTime)을 기준으로 내림차순 정렬됩니다.
     *
     * @param email 조회할 사용자의 이메일 주소
     * @return 사용자가 속한 ChatRoomMemberEntity 의 리스트
     */
    @Override
    public List<ChatRoomMemberEntity> myChatRoomList(String email) {
        QChatRoomMemberEntity chatRoomMember = QChatRoomMemberEntity.chatRoomMemberEntity;
        QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;

        return jpa
                .selectFrom(chatRoomMember)
                .join(chatRoomMember.chatRoom, chatRoom)
                .where(
                        chatRoomMember.member.email.eq(email)
                        , chatRoom.chatRoomType.ne(ChatRoomType.DELETE)
                        , chatRoomMember.status.ne(ChatRoomMemberStatus.EXIT)
                )
                .orderBy(chatRoom.lastChatTime.desc())
                .fetch();
    }
}
