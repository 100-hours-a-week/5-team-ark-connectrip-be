package connectripbe.connectrip_be.chat.config.handler;

import connectripbe.connectrip_be.auth.jwt.JwtProvider;
import connectripbe.connectrip_be.chat.config.service.ChatSessionService;
import connectripbe.connectrip_be.chat.dto.ChatRoomSessionDto;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final ChatSessionService chatSessionService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;

        try {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                handleConnect(accessor);
            } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                handleSubscribe(accessor);
            } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                handleDisconnect(accessor);
            }
        } catch (Exception e) {
            log.error("Error occurred while processing STOMP message: {}", e.getMessage());
            throw e;
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String accessToken = resolveTokenFromCookie(accessor);

        if (!jwtProvider.validateToken(accessToken)) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtProvider.getMemberIdFromToken(accessToken);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, null);
        accessor.setUser(authenticationToken);
        log.info("User {} connected via WebSocket", userId);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Long chatRoomId = extractChatRoomIdFromDestination(Objects.requireNonNull(destination));
        Long memberId = Long.parseLong(Objects.requireNonNull(accessor.getUser()).getName());
        String sessionId = accessor.getSessionId();

        chatSessionService.saveUserSession(chatRoomId, memberId, sessionId);
        log.info("User {} subscribed to chat room {}, session ID: {}", memberId, chatRoomId, sessionId);

    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        ChatRoomSessionDto sessionDto = chatSessionService.getUserSession(sessionId);

        if (sessionDto == null) {
            log.warn("Session not found for sessionId: {}", sessionId);
            throw new GlobalException(ErrorCode.INVALID_SESSION);
        }

        Long chatRoomId = sessionDto.chatRoomId();
        Long memberId = sessionDto.memberId();
        chatSessionService.updateLastReadMessage(memberId, chatRoomId);
        chatSessionService.removeUserSession(sessionId);
        log.info("User {} disconnected from chat room {}, session ID: {}", memberId, chatRoomId, sessionId);
    }

    // 쿠키에서 accessToken 추출
    private String resolveTokenFromCookie(StompHeaderAccessor accessor) {
        String cookieHeader = accessor.getFirstNativeHeader("Cookie");
        log.info("cookieHeader: {}", cookieHeader);
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                int equalSignIndex = cookie.indexOf("=");
                if (equalSignIndex > 0) {
                    String cookieName = cookie.substring(0, equalSignIndex).trim();
                    String cookieValue = cookie.substring(equalSignIndex + 1).trim();

                    if ("accessToken".equals(cookieName)) {
                        log.info("accessToken found: {}", cookieValue);
                        return cookieValue;
                    }
                }
            }
        }

        log.error("accessToken not found in cookies");
        throw new GlobalException(ErrorCode.TOKEN_NOT_FOUND);
    }


    // 쿠키에서 refreshToken 추출
    private String resolveRefreshTokenFromCookie(StompHeaderAccessor accessor) {
        String cookieHeader = accessor.getFirstNativeHeader("Cookie");
        if (cookieHeader != null) {
            for (String cookie : cookieHeader.split(";")) {
                String[] cookiePair = cookie.split("=");
                if ("refreshToken".equals(cookiePair[0].trim())) {
                    return cookiePair[1].trim();
                }
            }
        }
        return null;
    }


    private Long extractChatRoomIdFromDestination(String destination) {
        // "/sub/chat/room/{chatRoomId}" 형식에서 chatRoomId 추출
        return Long.parseLong(destination.substring(destination.lastIndexOf('/') + 1));
    }


}
