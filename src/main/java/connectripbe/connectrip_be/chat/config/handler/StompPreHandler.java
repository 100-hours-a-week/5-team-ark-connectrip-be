package connectripbe.connectrip_be.chat.config.handler;

import connectripbe.connectrip_be.auth.jwt.JwtProvider;
import connectripbe.connectrip_be.chat.config.service.ChatSessionService;
import connectripbe.connectrip_be.chat.dto.ChatRoomSessionDto;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.service.RedisService;
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
    private final RedisService redisService;

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
        // 세션 속성에서 쿠키 가져오기
        String cookies = (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("cookie");
        log.info("Cookie: {}", cookies);

        if (cookies == null) {
            throw new GlobalException(ErrorCode.TOKEN_NOT_FOUND);
        }

        String accessToken = resolveTokenFromCookie(cookies);  // 쿠키에서 accessToken 추출

        if (!jwtProvider.validateToken(accessToken)) {
            throw new GlobalException(ErrorCode.INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberIdFromToken(accessToken);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberId, null, null);
        accessor.setUser(authenticationToken);

        log.info("[WS] memberId: {} connected via WebSocket  ", memberId);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Long chatRoomId = extractChatRoomIdFromDestination(Objects.requireNonNull(destination));
        Long memberId = Long.parseLong(Objects.requireNonNull(accessor.getUser()).getName());
        String sessionId = accessor.getSessionId();

        chatSessionService.saveUserSession(chatRoomId, memberId, sessionId);
        log.info("SUBSCRIBE memberID {}, chatRoomId: {}, sessionID: {}", memberId, chatRoomId, sessionId);

    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        ChatRoomSessionDto sessionDto = chatSessionService.getUserSession(sessionId);

        if (sessionDto == null) {
            log.warn("Session not found for sessionId: {}", sessionId);
            throw new GlobalException(ErrorCode.NOT_FOUND_SESSION);
        }

        chatSessionService.updateLastReadMessage(sessionDto);
        log.info("DISCONNECT- Last Message save."
                        + " memberID {}, chatRoomId: {}, sessionID: {}",
                sessionDto.memberId(), sessionDto.chatRoomId()
                , sessionId);
        chatSessionService.removeUserSession(sessionId, sessionDto.chatRoomId(), sessionDto.memberId());
    }

    // 쿠키에서 accessToken 추출
    private String resolveTokenFromCookie(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] cookiePair = cookie.split("=", 2);
            if (cookiePair.length == 2 && "accessToken".equals(cookiePair[0].trim())) {
                return cookiePair[1].trim();
            }
        }
        throw new GlobalException(ErrorCode.TOKEN_NOT_FOUND);

    }


    private Long extractChatRoomIdFromDestination(String destination) {
        // "/sub/chat/room/{chatRoomId}" 형식에서 chatRoomId 추출
        return Long.parseLong(destination.substring(destination.lastIndexOf('/') + 1));
    }


}
