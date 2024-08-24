package connectripbe.connectrip_be.accompany_status.web;

import connectripbe.connectrip_be.accompany_status.response.AccompanyStatusResponse;
import connectripbe.connectrip_be.accompany_status.service.AccompanyStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// info-noah: 백엔드 팀과 상의 후 accompanyPost 컨트롤러와 병합

@RestController
@RequestMapping("/api/v1/accompany/posts")
@RequiredArgsConstructor
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
