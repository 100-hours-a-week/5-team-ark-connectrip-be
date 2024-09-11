package connectripbe.connectrip_be.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class SearchChatRoomWithToggleResponse {

    @JsonProperty("isLocationSharingEnabled")
    private final boolean isLocationSharingEnabled;

    @JsonProperty("chatRoomMemberLocations")
    private final List<ChatRoomMemberLocationDto> chatRoomMemberLocationDtos;

    public SearchChatRoomWithToggleResponse(
            boolean isLocationSharingEnabled
    ) {
        this.isLocationSharingEnabled = isLocationSharingEnabled;
        this.chatRoomMemberLocationDtos = new ArrayList<>();
    }

    public void addChatRoomMemberLocationDto(ChatRoomMemberLocationDto chatRoomMemberLocationDto) {
        chatRoomMemberLocationDtos.add(chatRoomMemberLocationDto);
    }
}
