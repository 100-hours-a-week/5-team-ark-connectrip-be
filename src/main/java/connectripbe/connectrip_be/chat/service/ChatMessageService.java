package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.dto.ChatMessageRequest;
import connectripbe.connectrip_be.chat.dto.ChatMessageResponse;
import java.util.List;

public interface ChatMessageService {

    ChatMessageResponse saveMessage(ChatMessageRequest chatMessage);

    List<ChatMessageResponse> getMessages(Long chatRoomId);
}
