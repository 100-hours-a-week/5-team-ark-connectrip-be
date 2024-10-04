package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.chat.dto.ChatRoomEnterDto;
import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatUnreadMessagesResponse;
import connectripbe.connectrip_be.chat.service.ChatRoomService;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatRoom")
@RequiredArgsConstructor
@Tag(name = "Chat Room", description = "채팅방 관리")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomListResponse>> getChatRoomList(
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(chatRoomService.getChatRoomList(memberId));
    }

    // 해당 채팅방 참여자 목록 조회
    @GetMapping("/{chatRoomId}/members")
    public ResponseEntity<?> getChatRoomMembers(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long memberId
    ) {
        return ResponseEntity.ok(chatRoomService.getChatRoomMembers(chatRoomId, memberId));
    }

    // 해당 채팅방 나가기
    @PostMapping("/{chatRoomId}/exit")
    public ResponseEntity<?> exitChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long memberId
    ) {
        chatRoomService.exitChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("채팅방 나가기 완료");
    }

    // 채팅방 입장
    @GetMapping("/{chatRoomId}/enter")
    public ResponseEntity<GlobalResponse<ChatRoomEnterDto>> enterChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long memberId
    ) {
        return
                ResponseEntity
                        .ok()
                        .body(new GlobalResponse<>("SUCCESS", chatRoomService.enterChatRoom(chatRoomId, memberId)));


    }


    // 채팅방 중 새로운 메시지 온다면 표시
    @GetMapping("/new")
    public ResponseEntity<GlobalResponse<ChatUnreadMessagesResponse>> hasUnreadMessagesAcrossRooms(
            @AuthenticationPrincipal Long memberId
    ) {
        return
                ResponseEntity
                        .ok()
                        .body(new GlobalResponse<>("SUCCESS", chatRoomService.hasUnreadMessagesAcrossRooms(memberId)));
    }
}
