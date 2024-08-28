package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.chat.dto.ChatMessageDto;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/message")
    public void senMessage(@Payload ChatMessageDto chatMessage) {
        ChatMessageDto savedMessage = chatMessageService.saveMessage(chatMessage);
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + savedMessage.chatRoomId(), savedMessage);
    }

    @GetMapping("/api/v1/chatRoom/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatRoomMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatRoomId));
    }
}
