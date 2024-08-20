package connectripbe.connectrip_be.member.web;

import connectripbe.connectrip_be.auth.config.LoginUser;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoResponse;
import connectripbe.connectrip_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberHeaderInfoResponse getMemberHeaderInfo(@LoginUser String email) {
        return memberService.getMemberHeaderInfo(email);
    }
}
