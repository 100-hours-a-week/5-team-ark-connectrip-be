package connectripbe.connectrip_be.accompany_member.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.service.impl.ChatRoomServiceImpl;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.entity.enums.AccompanyArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ChatRoomServiceImplTest {

      @InjectMocks
      private ChatRoomServiceImpl chatRoomService;

      @Mock
      private ChatRoomMemberRepository chatRoomMemberRepository;

      @BeforeEach
      void setUp() {
            MockitoAnnotations.openMocks(this);
      }

      @Test
      @DisplayName("사용자가 참여한 채팅방 목록을 조회하여 반환")
      void testGetChatRoomList_ReturnsList() {
            String email = "test@example.com";

            AccompanyPostEntity accompanyPost = AccompanyPostEntity.builder()
                    .id(1L)
                    .title("Test Title")
                    .accompanyArea(AccompanyArea.SEOUL.toString())
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .build();

            ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                    .id(1L)
                    .accompanyPost(accompanyPost)
                    .build();

            ChatRoomMemberEntity memberEntity = ChatRoomMemberEntity.builder()
                    .chatRoom(chatRoom)
                    .build();

            // chatRoomMemberRepository가 특정 이메일에 대한 데이터를 반환하도록 설정
            when(chatRoomMemberRepository.findByMember_Email(email)).thenReturn(List.of(memberEntity));

            List<ChatRoomListResponse> result = chatRoomService.getChatRoomList(1L);

            assertEquals(1, result.size());  // 반환된 리스트의 크기 검증
            assertEquals(1L, result.get(0).chatRoomId());  // chatRoomId 검증
            assertEquals(1L, result.get(0).accompanyPostId());  // accompanyPostId 검증
            assertEquals("Test Title", result.get(0).accompanyPostTitle());  // accompanyPostTitle 검증
            assertEquals("SEOUL", result.get(0).accompanyArea());  // accompanyArea 검증
      }

      @Test
      @DisplayName("채팅방이 없을 경우 빈 리스트 반환")
      void testGetChatRoomList_ReturnsEmptyListWhenNoRooms() {
            String email = "test@example.com";
            when(chatRoomMemberRepository.findByMember_Email(email)).thenReturn(List.of());

            List<ChatRoomListResponse> result = chatRoomService.getChatRoomList(1L);

            assertEquals(0, result.size());
      }
}