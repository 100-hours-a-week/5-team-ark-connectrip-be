package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatRoom")
@RequiredArgsConstructor
public class ChatRoomController {

      private final ChatRoomService chatRoomService;

      @GetMapping("/list")
      public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList(@LoginUser String email) {
            return ResponseEntity.ok(chatRoomService.getChatRoomList(email));
      }

}
