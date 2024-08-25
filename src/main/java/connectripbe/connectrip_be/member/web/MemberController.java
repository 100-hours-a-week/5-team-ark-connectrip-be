package connectripbe.connectrip_be.member.web;

import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(
            @RequestParam String email
    ) {
        return memberService.checkDuplicateEmail(email);
    }

    @GetMapping("/check-nickname")
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        return memberService.checkDuplicateNickname(nickname);
    }

    @GetMapping("/me")
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(
            @AuthenticationPrincipal Long id
    ) {
        return memberService.getMemberHeaderInfo(id);
    }

    // fixme-noah: 2024-08-21, 엉망 코드
    @PostMapping("/first")
    public ResponseEntity<GlobalResponse<MemberHeaderInfoDto>> firstUpdateMember(
            @AuthenticationPrincipal Long id,
            @RequestBody FirstUpdateMemberRequest request
    ) {
        GlobalResponse<MemberHeaderInfoDto> firstUpdateMemberResponse = memberService.getFirstUpdateMemberResponse(id, request);

        return ResponseEntity.status(firstUpdateMemberResponse.message().equals("SUCCESS") ? 200 : 409).body(firstUpdateMemberResponse);
    }
}
