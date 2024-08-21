package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import java.util.List;

public interface ChatRoomService {

      List<ChatRoomListResponse> getChatRoomList(String email);

}
