package connectripbe.connectrip_be.member.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-email")
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(@RequestParam String email) {
        return memberService.checkDuplicateEmail(email);
    }

    @GetMapping("/me")
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(@LoginUser String email) {
        return memberService.getMemberHeaderInfo(email);
    }

    @PostMapping("/first")
    public GlobalResponse<MemberHeaderInfoDto> firstUpdateMember(@LoginUser String email, @RequestBody FirstUpdateMemberRequest request) {
        return memberService.getFirstUpdateMemberResponse(email, request);
    }
}
