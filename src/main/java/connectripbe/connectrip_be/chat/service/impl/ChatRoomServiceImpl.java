package connectripbe.connectrip_be.chat.service.impl;

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
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomServiceImpl.class);
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    private final ChatRoomMemberService chatRoomMemberService;

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
     * 특정 게시물에 대한 채팅방을 생성하고, 게시물 작성자를 해당 채팅방에 자동으로 참여시킵니다.
     *
     * @param postId  채팅방을 생성할 게시물의 ID
     * @param memberId  채팅방에 자동으로 참여시킬 게시물 작성자의 ID
     * @throws GlobalException  게시물을 찾을 수 없는 경우 발생하는 예외
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

    }
}
