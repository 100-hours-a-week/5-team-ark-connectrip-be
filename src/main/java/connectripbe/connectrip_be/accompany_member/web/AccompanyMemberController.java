package connectripbe.connectrip_be.accompany_member.web;

import connectripbe.connectrip_be.accompany_member.dto.AccompanyMemberResponse;
import connectripbe.connectrip_be.accompany_member.service.AccompanyMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accompany")
@RequiredArgsConstructor
public class AccompanyMemberController {

      private final AccompanyMemberService accompanyMemberService;

      @GetMapping("{accompanyPostId}/members")
      public ResponseEntity<List<AccompanyMemberResponse>> getAccompanyMemberList(@PathVariable Long accompanyPostId) {
            return ResponseEntity.ok(accompanyMemberService.getAccompanyMemberList(accompanyPostId));
      }
}
