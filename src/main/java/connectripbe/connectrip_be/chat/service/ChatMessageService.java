package connectripbe.connectrip_be.chat.service;

import connectripbe.connectrip_be.chat.dto.ChatMessageDto;
import java.util.List;

public interface ChatMessageService {

    ChatMessageDto saveMessage(ChatMessageDto chatMessage);

    List<ChatMessageDto> getMessages(Long chatRoomId);
}
