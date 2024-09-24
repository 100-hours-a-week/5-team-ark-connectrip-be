package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.chat.dto.ChatMessageRequest;
import connectripbe.connectrip_be.chat.dto.ChatMessageResponse;
import connectripbe.connectrip_be.chat.service.ChatMessageService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MongoTemplate mongoTemplate;

    @MessageMapping("/chat/room/{chatRoomId}")
    public void sendMessage(@Payload ChatMessageRequest chatMessage,
                            @DestinationVariable("chatRoomId") Long chatRoomId,
                            Principal principal) {
        ChatMessageResponse savedMessage = chatMessageService.saveMessage(chatMessage, chatRoomId,
                Long.parseLong(principal.getName()));
        simpMessagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomId, savedMessage);
    }

    @GetMapping("/api/v1/chatRoom/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatRoomMessages(
            @PathVariable Long chatRoomId,
            @RequestParam(required = false) String lastMessageId,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(chatMessageService.getMessagesAfterId(chatRoomId, lastMessageId, size));
    }


}