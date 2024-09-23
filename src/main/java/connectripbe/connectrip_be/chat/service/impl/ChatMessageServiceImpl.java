package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatMessageRequest;
import connectripbe.connectrip_be.chat.dto.ChatMessageResponse;
import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import connectripbe.connectrip_be.chat.repository.ChatMessageRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.service.RedisService;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final String CHAT_ROOM_LIST_KEY_PREFIX = "chat_room_list: ";

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RedisService redisService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request, Long chatRoomId, Long memberId) {
        // 채팅 수신 유저 정보 조회
        MemberEntity member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 유저가 보낸 메세지 전송 여부
        boolean infoFlag = (request.infoFlag() != null) ? request.infoFlag() : false;

        ChatMessage chatMessage =
                ChatMessage.builder()
                        .type(MessageType.TALK)
                        .chatRoomId(chatRoomId)
                        .senderId(member.getId())
                        .senderNickname(member.getNickname())
                        .senderProfileImage(member.getProfileImagePath())
                        .content(request.content())
                        .infoFlag(infoFlag)
                        .build();

        ChatMessage saved = chatMessageRepository.save(chatMessage);

        ChatRoomEntity chatRoom = chatRoomRepository.findByIdWithPost(chatRoomId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        log.info("ID: {}", saved.getId());
        // 채팅방 테이블에 채팅 마지막 내용과 마지막 시간 업데이트
        chatRoom.updateLastChatMessage(saved.getContent(), saved.getCreatedAt(), saved.getId());
        chatRoomRepository.save(chatRoom);

        String title = chatRoom.getAccompanyPost().getTitle();

        // 채팅방에 입장하지 않은 사람들에게 알림 발송
        sendMessageToNotification(chatRoomId, ChatMessageResponse.notificationFromEntity(saved, title));
        log.info("리스폰스 {}", ChatMessageResponse.notificationFromEntity(saved, title));

        return ChatMessageResponse.fromEntity(saved);
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long chatRoomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(ChatMessageResponse::fromEntity)
                .toList();
    }

    private void sendMessageToNotification(Long chatRoomId, ChatMessageResponse message) {
        // 채팅방에 입장한 사용자 세션 리스트 조회
        List<Object> activeSessionIds = redisService.getListData(CHAT_ROOM_LIST_KEY_PREFIX + chatRoomId);
        log.info("activeSessionIds: {}", activeSessionIds);

        // 채팅방 회원 리스트
        List<Long> memberIds = chatRoomMemberRepository.findMemberIdsByChatRoomId(chatRoomId);

        // 세션 ID를 Long 타입으로 변환
        List<Long> activeMemberIds = activeSessionIds.stream()
                .map(sessionId -> Long.valueOf(sessionId.toString()))
                .toList();

        // 채팅방 회원 리스트 중 채팅방에 입장하지 않은 사람들에게 알림 발송
        memberIds.stream()
                .filter(memberId -> !activeMemberIds.contains(memberId))
                .forEach(memberId -> {
                    // 알림 발송
                    simpMessagingTemplate.convertAndSend("/sub/member/notification/" + memberId, message);
                    log.info("발송 성공: {}", memberId);
                });
    }

}
