package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.chat.dto.ChatMessageRequest;
import connectripbe.connectrip_be.chat.dto.ChatMessageResponse;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/room/{chatRoomId}")
    public void sendMessage(@Payload ChatMessageRequest chatMessage,
                            @DestinationVariable("chatRoomId") Long chatRoomId,
                            @AuthenticationPrincipal Long memberId) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(chatMessage, chatRoomId, memberId);
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomId, savedMessage);
    }

    @GetMapping("/api/v1/chatRoom/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatRoomMessages(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatMessageService.getMessages(chatRoomId));
    }
}