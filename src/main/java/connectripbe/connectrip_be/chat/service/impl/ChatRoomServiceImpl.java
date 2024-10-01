package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.accompany.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany.status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.chat.dto.ChatRoomEnterDto;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatRoomMemberResponse;
import connectripbe.connectrip_be.chat.dto.ChatUnreadMessagesResponse;
import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomMemberService;
import connectripbe.connectrip_be.chat.service.ChatRoomService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.pending_list.entity.PendingListEntity;
import connectripbe.connectrip_be.pending_list.entity.type.PendingStatus;
import connectripbe.connectrip_be.pending_list.repository.PendingListRepository;
import connectripbe.connectrip_be.review.repository.AccompanyReviewRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final AccompanyPostRepository accompanyPostRepository;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;
    private final PendingListRepository pendingListRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AccompanyReviewRepository reviewRepository;

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

        List<ChatRoomListResponse> chatRoomListResponses = new ArrayList<>();

        for (ChatRoomMemberEntity member : chatRoomMembers) {
            ChatRoomEntity chatRoom = member.getChatRoom();
            boolean hasUnreadMessages = hasUnreadMessage(chatRoom.getId(), memberId);
            ChatRoomListResponse response = ChatRoomListResponse.fromEntity(chatRoom, hasUnreadMessages);
            chatRoomListResponses.add(response);
        }

        return chatRoomListResponses;
    }

    /**
     * 주어진 채팅방의 참여자 목록을 조회하여 반환하는 메서드. 채팅방 ID와 사용자 ID를 기반으로 해당 채팅방의 모든 참여자를 조회하고, 로그인한 사용자가 각 멤버에 대해 리뷰를 작성할 수 있는지 여부를
     * 함께 반환.
     *
     * @param chatRoomId 채팅방의 ID. 이 ID를 기반으로 해당 채팅방의 참여자를 조회.
     * @param memberId   현재 로그인한 사용자의 ID.
     * @return 채팅방의 참여자 목록을 포함하는 `List<ChatRoomMemberResponse>`. 각 응답에는 리뷰 작성 가능 여부도 포함됨. 채팅방에 참여한 사용자가 없을 경우 빈 리스트를 반환.
     */
    @Override
    public List<ChatRoomMemberResponse> getChatRoomMembers(Long chatRoomId, Long memberId) {

        List<ChatRoomMemberEntity> chatRoomMembers = chatRoomMemberRepository.findByChatRoom_Id(
                chatRoomId);

        List<Long> targetIds = reviewRepository.findAllTargetIdsByReviewerIdAndChatRoomId(memberId,
                chatRoomId);

        return chatRoomMembers.stream()
                .filter(member -> !Objects.equals(member.getStatus(),
                        ChatRoomMemberStatus.EXIT))
                .map(member -> chatRoomMemberResponse(member, memberId, targetIds))
                .toList();
    }

    /**
     * ChatRoomMemberEntity 를 기반으로 ChatRoomMemberResponse 객체를 생성하는 메서드. 해당 멤버가 리뷰 작성 대상인지 여부를 판단하여 응답에 포함.
     *
     * @param chatRoomMember  참여자 엔티티.
     * @param currentMemberId 현재 로그인한 사용자의 ID.
     * @param targetIds       현재 로그인한 사용자가 이미 리뷰를 작성한 멤버들의 ID 목록.
     * @return `ChatRoomMemberResponse` 객체. 각 참여자에 대해 리뷰 작성 가능 여부 포함.
     */
    private ChatRoomMemberResponse chatRoomMemberResponse(ChatRoomMemberEntity chatRoomMember,
                                                          Long currentMemberId,
                                                          List<Long> targetIds) {
        Long targetId = chatRoomMember.getMember().getId();

        boolean hasWrittenReview = targetIds.contains(targetId);

        boolean canWritReview = !targetId.equals(currentMemberId) && !hasWrittenReview;

        return ChatRoomMemberResponse.fromEntity(chatRoomMember, canWritReview);
    }

    /**
     * 특정 게시물에 대한 채팅방을 생성하고, 게시물 작성자를 해당 채팅방에 자동으로 참여. 생성된 채팅방에서 게시물 작성자는 초기 방장으로 설정.
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

        ChatMessage chatMessage = ChatMessage.builder()
                .type(MessageType.ENTER)
                .chatRoomId(chatRoom.getId())
                .senderId(memberId)
                .senderNickname(leaderMember.getMember().getNickname())
                .senderProfileImage(leaderMember.getMember().getProfileImagePath())
                .content(leaderMember.getMember().getNickname() + "님이 입장하셨습니다.")
                .infoFlag(true)
                .build();

        chatMessageRepository.save(chatMessage);
    }


    /**
     * 사용자가 채팅방에서 나가도록 처리. 주어진 채팅방 ID와 사용자 ID를 기반으로, 사용자가 해당 채팅방에서 나가게 하며, 방장이 나가는 경우에는 방장을 승계하고, 마지막 남은 멤버가 나가는 경우 채팅방
     * 상태를 'DELETE'로 변경.
     *
     * @param chatRoomId 나가려는 사용자가 속한 채팅방의 ID
     * @param memberId   나가려는 사용자의 ID
     * @throws GlobalException 채팅방이나 사용자가 존재하지 않거나, 방장 승계 중 문제가 발생한 경우
     */
    @Override
    @Transactional
    public void exitChatRoom(Long chatRoomId, Long memberId) {

        // 채팅방이 존재하는지 확인
        ChatRoomEntity chatRoom = getChatRoom(chatRoomId);

        // 사용자가 존재하는지 확인
        ChatRoomMemberEntity chatRoomMember = getRoomMember(chatRoomId, memberId);

        // 채팅방에서 나가기 전 pendingList 업데이트
        pendingListUpdate(chatRoomMember, chatRoom);

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


    @Override
    public ChatRoomEnterDto enterChatRoom(Long chatRoomId, Long memberId) {

        // 채팅방이 존재하는지 확인
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 사용자가 존재하는지 확인
        ChatRoomMemberEntity chatRoomMember = chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(chatRoomId,
                memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 채팅방을 나간 사용자가 다시 입장하는 경우
        if (!chatRoomMember.getStatus().equals(ChatRoomMemberStatus.ACTIVE)) {
            throw new GlobalException(ErrorCode.ALREADY_EXITED_CHAT_ROOM);
        }

        // 해당 게시물 상태
        AccompanyStatusEntity status = accompanyStatusJpaRepository.findTopByAccompanyPostEntityOrderByCreatedAtDesc(
                        chatRoom.getAccompanyPost())
                .orElseThrow(NotFoundAccompanyPostException::new);

        // 게시물이 삭제 되었는지
        boolean isPostExists = chatRoom.getAccompanyPost().getDeletedAt() == null;

        return ChatRoomEnterDto.fromEntity(chatRoom, status.getAccompanyStatusEnum().toString(), isPostExists);
    }

    private void pendingListUpdate(ChatRoomMemberEntity chatRoomMember, ChatRoomEntity chatRoom) {
        AccompanyPostEntity accompanyPost = chatRoomMember.getChatRoom().getAccompanyPost();

        // 방장이 아닌 경우에만 pendingList 업데이트
        if (!chatRoomMember.getMember().equals(chatRoom.getCurrentLeader().getMember())) {
            PendingListEntity pendingMember = pendingListRepository.findByAccompanyPostAndMember(
                            accompanyPost, chatRoomMember.getMember())
                    .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_LIST_NOT_FOUND));

            pendingMember.updateStatus(PendingStatus.EXIT_ROOM);
        }
    }

    private boolean hasUnreadMessage(Long chatRoomId, Long memberId) {
        ChatRoomMemberEntity chatMember = chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(
                chatRoomId, memberId).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        String lastReadMessageId = chatMember.getLastReadMessageId();

        ChatMessage latestMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
                .orElse(null);

        if (latestMessage == null) {
            // 만약 채팅방에 메시지가 없다면 읽지 않은 메시지도 없음
            return false;
        }

        // 마지막으로 읽은 메시지 이후에 새 메시지가 있다면 true 반환
        return lastReadMessageId == null || latestMessage.getId().compareTo(lastReadMessageId) > 0;
    }

    @Override
    public ChatUnreadMessagesResponse hasUnreadMessagesAcrossRooms(Long memberId) {
        List<ChatRoomMemberEntity> chatRooms = chatRoomMemberRepository.myChatRoomList(memberId);

        for (ChatRoomMemberEntity chatRoomMember : chatRooms) {
            ChatRoomEntity chatRoom = chatRoomMember.getChatRoom();
            String lastReadMessageId = chatRoomMember.getLastReadMessageId();

            // 채팅방의 마지막 메시지를 가져옴
            ChatMessage latestMessage = chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(
                            chatRoom.getId())
                    .orElse(null);

            if (latestMessage != null &&
                    (lastReadMessageId == null || latestMessage.getId().compareTo(lastReadMessageId) > 0)) {
                // 읽지 않은 메시지가 하나라도 있으면 true 반환
                return new ChatUnreadMessagesResponse(true);
            }
        }

        // 모든 채팅방을 확인했을 때 읽지 않은 메시지가 없으면 false 반환
        return new ChatUnreadMessagesResponse(false);
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
