package connectripbe.connectrip_be.accompany.status.web;

import connectripbe.connectrip_be.accompany.status.response.AccompanyStatusResponse;
import connectripbe.connectrip_be.accompany.status.service.AccompanyStatusServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// info-noah: 백엔드 팀과 상의 후 accompanyPost 컨트롤러와 병합

@RestController
@RequestMapping("/api/v1/accompany/posts")
@RequiredArgsConstructor
@Tag(name = "Accompany Status", description = "Accompany 게시물 상태 관리")
public class AccompanyStatusController {

    private final AccompanyStatusServiceImpl accompanyStatusServiceImpl;

    @GetMapping("/{id}/status")
    public AccompanyStatusResponse getAccompanyStatus(
            @PathVariable("id") Long id
    ) {
        return accompanyStatusServiceImpl.getAccompanyStatus(id);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateAccompanyStatus(
            @AuthenticationPrincipal Long memberId,
            @PathVariable("id") Long id
    ) {
        accompanyStatusServiceImpl.updateAccompanyStatus(memberId, id);

        return ResponseEntity.ok().build();
    }
}
