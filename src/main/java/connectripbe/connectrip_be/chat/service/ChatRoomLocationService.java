package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.dto.SearchChatRoomWithToggleResponse;

public interface ChatRoomLocationService {

    SearchChatRoomWithToggleResponse searchChatRoomLocationByChatRoomIdAndMember(
            Long chatRoomId,
            Long memberId);

    SearchChatRoomWithToggleResponse updateNotShareLocation(
            Long chatRoomId,
            Long memberId);

    SearchChatRoomWithToggleResponse updateShareLocation(
            Long chatRoomId,
            Long memberId,
            Double latitude,
            Double longitude);

    void updateMyLocation(
            Long memberId,
            Long chatRoomId,
            Double latitude,
            Double longitude);
}
