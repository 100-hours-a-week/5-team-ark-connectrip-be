package connectripbe.connectrip_be.Review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // 기본 생성자 필요
@AllArgsConstructor  // 모든 필드를 포함하는 생성자를 Lombok이 자동으로 생성
@Builder  // 빌더 패턴을 사용하기 위함
public class AccompanyReviewRequest {
    private Long reviewerId;
    private Long targetId;
    private Long accompanyPostId;
    private String content;
}
