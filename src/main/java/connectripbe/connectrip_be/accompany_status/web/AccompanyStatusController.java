package connectripbe.connectrip_be.accompany_status.web;

import connectripbe.connectrip_be.accompany_status.response.AccompanyStatusResponse;
import connectripbe.connectrip_be.accompany_status.service.AccompanyStatusService;
import connectripbe.connectrip_be.auth.config.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// info-noah: 백엔드 팀과 상의 후 accompanyPost 컨트롤러와 병합

@RestController
@RequestMapping("/api/v1/accompany/posts")
@RequiredArgsConstructor
public class AccompanyStatusController {

    private final AccompanyStatusService accompanyStatusService;

    @GetMapping("/{id}/status")
    public AccompanyStatusResponse getAccompanyStatus(@PathVariable("id") Long id) {
        return accompanyStatusService.getAccompanyStatus(id);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateAccompanyStatus(@LoginUser String memberEmail, @PathVariable("id") Long id) {
        accompanyStatusService.updateAccompanyStatus(memberEmail, id);

        return ResponseEntity.ok().build();
    }
}
