package connectripbe.connectrip_be.chat.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class SearchChatRoomWithToggleResponse {

    private final boolean isLocationTrackingEnabled;
    private final List<ChatRoomMemberLocationDto> chatRoomMemberLocationDtos;

    public SearchChatRoomWithToggleResponse(
            boolean isLocationTrackingEnabled
    ) {
        this.isLocationTrackingEnabled = isLocationTrackingEnabled;
        this.chatRoomMemberLocationDtos = new ArrayList<>();
    }

    public void addChatRoomMemberLocationDto(ChatRoomMemberLocationDto chatRoomMemberLocationDto) {
        chatRoomMemberLocationDtos.add(chatRoomMemberLocationDto);
    }
}
