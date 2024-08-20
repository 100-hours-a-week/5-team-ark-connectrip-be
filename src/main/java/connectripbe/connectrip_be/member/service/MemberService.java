package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.member.dto.MemberHeaderInfoResponse;

public interface MemberService {

    MemberHeaderInfoResponse getMemberHeaderInfo(String email);
}
