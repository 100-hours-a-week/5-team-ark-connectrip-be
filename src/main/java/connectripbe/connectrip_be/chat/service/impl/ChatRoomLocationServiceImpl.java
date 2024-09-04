package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatRoomMemberLocationDto;
import connectripbe.connectrip_be.chat.dto.SearchChatRoomWithToggleResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomLocationService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomLocationServiceImpl implements ChatRoomLocationService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public SearchChatRoomWithToggleResponse searchChatRoomLocationByChatRoomIdAndMember(
            Long chatRoomId,
            Long memberId
    ) {
        // 1. 채팅방 id와 사용자 id를 통해 채팅방에 소속된 사용자 정보를 찾는다.
        ChatRoomMemberEntity chatRoomMemberEntity = getChatRoomMemberEntity(chatRoomId, memberId);

        // 2-1. 내 위치 공유를 허용하지 않는다면 false와 빈 리스트를 전달한다.
        if (!chatRoomMemberEntity.isLocationTrackingEnabled()) {
            return new SearchChatRoomWithToggleResponse(false);
        }

        // 2-2. 내 위치 공유를 허용한다면 true와 현 채팅방에서 활동 중이며, 위치 공유를 허용하는 동행자들의 정보를 리스트에 저장한다.

        List<ChatRoomMemberEntity> chatRoomMemberEntities = chatRoomMemberRepository.findAllByChatRoom_IdAndStatusAndIsLocationTrackingEnabled(
                chatRoomId,
                ChatRoomMemberStatus.ACTIVE,
                true
        );

        SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = new SearchChatRoomWithToggleResponse(true);

        for (ChatRoomMemberEntity roomMemberEntity : chatRoomMemberEntities) {
            searchChatRoomWithToggleResponse.addChatRoomMemberLocationDto(
                    ChatRoomMemberLocationDto.fromEntity(roomMemberEntity));
        }

        return searchChatRoomWithToggleResponse;
    }

    @Override
    public SearchChatRoomWithToggleResponse updateNotShareLocation(
            Long chatRoomId,
            Long memberId) {
        // 1. 채팅방 id와 사용자 id를 통해 채팅방에 소속된 사용자 정보를 찾는다.
        ChatRoomMemberEntity chatRoomMemberEntity = getChatRoomMemberEntity(chatRoomId, memberId);

        chatRoomMemberEntity.toggleLocationTracking();

        return new SearchChatRoomWithToggleResponse(false);
    }

    @Override
    public SearchChatRoomWithToggleResponse updateShareLocation(
            Long chatRoomId,
            Long memberId,
            Double latitude,
            Double longitude) {
        // 1. 채팅방 id와 사용자 id를 통해 채팅방에 소속된 사용자 정보를 찾는다.
        ChatRoomMemberEntity chatRoomMemberEntity = getChatRoomMemberEntity(chatRoomId, memberId);

        chatRoomMemberEntity.toggleLocationTracking();

        // 2-2. 위치 공유를 허용하는 동행자들의 정보를 리스트에 저장한다.

        // 2-2-1. 내 마지막 위치 갱신
        chatRoomMemberEntity.updateLocation(latitude, longitude);

        // 2-2-2. 정보 조회
        List<ChatRoomMemberEntity> chatRoomMemberEntities = chatRoomMemberRepository.findAllByChatRoom_IdAndStatusAndIsLocationTrackingEnabled(
                chatRoomId,
                ChatRoomMemberStatus.ACTIVE,
                true
        );

        SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = new SearchChatRoomWithToggleResponse(true);

        for (ChatRoomMemberEntity roomMemberEntity : chatRoomMemberEntities) {
            searchChatRoomWithToggleResponse.addChatRoomMemberLocationDto(
                    ChatRoomMemberLocationDto.fromEntity(roomMemberEntity));
        }

        return searchChatRoomWithToggleResponse;
    }

    private ChatRoomMemberEntity getChatRoomMemberEntity(
            Long chatRoomId,
            Long memberId
    ) {
        return chatRoomMemberRepository.findByChatRoom_IdAndMember_Id(chatRoomId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
