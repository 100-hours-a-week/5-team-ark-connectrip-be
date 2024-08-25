package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany_status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatRoomMemberResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;

import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomMemberService;
import connectripbe.connectrip_be.chat.service.ChatRoomService;


import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    private final ChatRoomMemberService chatRoomMemberService;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;

    /**
     * 사용자가 참여한 채팅방 목록을 조회하여 반환하는 메서드. 주어진 사용자의 이메일 주소를 기반으로 해당 사용자가 참여한 모든 채팅방을 조회
     *
     * @param memberId 사용자의 아이디. 이 이메일을 기반으로 해당 사용자가 참여한 채팅방을 조회
     * @return 사용자가 참여한 채팅방의 목록을 포함하는 `List<ChatRoomListResponse>` 사용자가 참여한 채팅방이 없을 경우 빈 리스트를 반환
     */
    @Override
    public List<ChatRoomListResponse> getChatRoomList(Long memberId) {
        // 사용자 참여한 모든 ChatRoomMemberEntity 조회
        List<ChatRoomMemberEntity> chatRoomMembers = chatRoomMemberRepository.myChatRoomList(
                memberId);

        return chatRoomMembers.stream()
                .map(member -> ChatRoomListResponse.fromEntity(member.getChatRoom()))
                .toList();
    }

    /**
     * 주어진 채팅방의 참여자 목록을 조회하여 반환하는 메서드. 주어진 채팅방의 ID를 기반으로 해당 채팅방의 모든 참여자를 조회
     *
     * @param chatRoomId 채팅방의 ID. 이 ID를 기반으로 해당 채팅방의 참여자를 조회
     * @return 채팅방의 참여자 목록을 포함하는 `List<ChatRoomMemberResponse>` 채팅방에 참여한 사용자가 없을 경우 빈 리스트를 반환
     */
    @Override
    public List<ChatRoomMemberResponse> getChatRoomMembers(Long chatRoomId) {

        List<ChatRoomMemberEntity> chatRoomMembers = chatRoomMemberRepository.findByChatRoom_Id(
                chatRoomId);

        return chatRoomMembers.stream()
                .filter(member -> !Objects.equals(member.getStatus(),
                        ChatRoomMemberStatus.EXIT))
                .map(ChatRoomMemberResponse::fromEntity)
                .toList();
    }

    /**
     * 특정 게시물에 대한 채팅방을 생성하고, 게시물 작성자를 해당 채팅방에 자동으로 참여.
     * 생성된 채팅방에서 게시물 작성자는 초기 방장으로 설정.
     *
     * @param postId   채팅방을 생성할 게시물의 ID
     * @param memberId 채팅방에 자동으로 참여시킬 게시물 작성자의 ID
     * @throws GlobalException 게시물을 찾을 수 없거나, 게시물 작성자를 채팅방에 참여시키는 데 문제가 발생한 경우 발생하는 예외
     */
    @Override
    public void createChatRoom(Long postId, Long memberId) {
        ChatRoomEntity chatRoom = ChatRoomEntity
                .builder()
                .accompanyPost(accompanyPostRepository.findById(postId)
                        .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND)))
                .chatRoomType(ChatRoomType.ACTIVE)
                .build();

        // 채팅방 생성
        chatRoomRepository.save(chatRoom);

        // 채팅방에 게시물 작성자 자동 참여
        chatRoomMemberService.jointChatRoom(chatRoom.getId(), memberId);

        // 채팅방 방장 초기 설정
        ChatRoomMemberEntity leaderMember = chatRoomMemberRepository
                .findByChatRoom_IdAndMember_Id(chatRoom.getId(), memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_MEMBER_NOT_FOUND));

        // 방장 설정
        chatRoom.setInitialLeader(leaderMember);
    }


    /**
     * 사용자가 채팅방에서 나가도록 처리.
     * 주어진 채팅방 ID와 사용자 ID를 기반으로, 사용자가 해당 채팅방에서 나가게 하며,
     * 방장이 나가는 경우에는 방장을 승계하고, 마지막 남은 멤버가 나가는 경우 채팅방 상태를 'DELETE'로 변경.
     *
     * @param chatRoomId 나가려는 사용자가 속한 채팅방의 ID
     * @param memberId 나가려는 사용자의 ID
     * @throws GlobalException 채팅방이나 사용자가 존재하지 않거나, 방장 승계 중 문제가 발생한 경우
     */
    @Override
    @Transactional
    public void exitChatRoom(Long chatRoomId, Long memberId) {

        // 채팅방이 존재하는지 확인
        ChatRoomEntity chatRoom = getChatRoom(chatRoomId);

        // 사용자가 존재하는지 확인
        ChatRoomMemberEntity chatRoomMember = getRoomMember(chatRoomId, memberId);

        // 사용자가 채팅방에서 나가기
        chatRoomMember.exitChatRoom();

        // 현재 채팅방에 남아있는 활성 상태의 멤버 수 확인
        int activeMemberCount = chatRoomMemberRepository
                .countByChatRoom_IdAndStatus(chatRoomId, ChatRoomMemberStatus.ACTIVE);

        if (activeMemberCount > 0) {
            // 다른 멤버가 있는 경우 방장 승계
            if (chatRoom.getCurrentLeader().equals(chatRoomMember)) {
                chatRoom.setInitialLeader(chatRoomMemberRepository
                        .findFirstByChatRoom_IdAndStatusOrderByCreatedAt(chatRoomId,
                                ChatRoomMemberStatus.ACTIVE)
                        .orElseThrow(
                                () -> new GlobalException(ErrorCode.CHAT_ROOM_MEMBER_NOT_FOUND)));
            }
        } else {
            // 마지막 남은 멤버가 나간 경우 채팅방 상태 변경
            chatRoom.changeChatRoomType(ChatRoomType.DELETE);

            // 게시물 상태 변경
            AccompanyStatusEntity accompanyStatusEntity = accompanyStatusJpaRepository
                    .findTopByAccompanyPostEntityOrderByCreatedAtDesc(chatRoom.getAccompanyPost())
                    .orElseThrow(NotFoundAccompanyPostException::new);

            // 채팅방이 닫히면 게시물 상태도 닫힘
            accompanyStatusEntity.changeAccompanyStatus(AccompanyStatusEnum.CLOSED);

            accompanyStatusJpaRepository.save(accompanyStatusEntity);
        }

        chatRoomRepository.save(chatRoom);
    }

    private ChatRoomEntity getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }

    private ChatRoomMemberEntity getRoomMember(Long chatRoomId, Long id) {
        return chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(chatRoomId, id)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
