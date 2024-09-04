package connectripbe.connectrip_be.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 연결할 수 있는 엔드포인트를 등록.
        registry.addEndpoint("/ws/init")  // 클라이언트가 "/ws" 엔드포인트로 WebSocket 연결을 시도할 수 있도록 설정.
                .setAllowedOrigins("http://localhost:3000")  // 허용할 도메인을 설정.
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // "/sub"로 시작하는 경로를 구독한 클라이언트에게 메시지를 전달.
        // 메시지를 구독하는 목적지 경로의 프리픽스를 "/sub"으로 설정.
        registry.enableSimpleBroker("/sub");

        // 메시지를 라우팅할 때 사용할 애플리케이션 목적지 프리픽스를 설정.
        // 클라이언트가 메시지를 보낼 때 "/pub" 프리픽스를 사용.
        registry.setApplicationDestinationPrefixes("/pub");


    }
}