package connectripbe.connectrip_be.accompany_member.service;

import connectripbe.connectrip_be.accompany_member.dto.AccompanyMemberResponse;
import java.util.List;

public interface AccompanyMemberService {

      // 해당 동행 참여자 목록 조회
      List<AccompanyMemberResponse> getAccompanyMemberList(Long accompanyId);


}

// 해당 동행 참여자 목록 조회
// 내 동행 목록 리스트

