package connectripbe.connectrip_be.accompany_member.service.impl;

import connectripbe.connectrip_be.accompany_member.dto.AccompanyMemberResponse;
import connectripbe.connectrip_be.accompany_member.entity.AccompanyMemberEntity;
import connectripbe.connectrip_be.accompany_member.repository.AccompanyMemberRepository;
import connectripbe.connectrip_be.accompany_member.service.AccompanyMemberService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccompanyMemberServiceImpl implements AccompanyMemberService {
      private final AccompanyMemberRepository accompanyMemberRepository;

      /**
       * 주어진 동행 게시물 ID(accompanyPostId)에 연관된 모든 참여자 목록을 조회하여 반환.
       * 해당 게시물에 참여한 회원들의 ID, 닉네임, 프로필 이미지 경로, 해당 채팅방에서의 상태를 포함하는
       * AccompanyMemberResponse 리스트를 반환.
       *
       * @param accompanyPostId 조회하고자 하는 동행 게시물의 ID
       * @return 동행 게시물에 참여한 모든 회원의 정보를 담은 AccompanyMemberResponse 리스트
       * @throws GlobalException 요청한 동행 게시물 ID가 유효하지 않을 경우 발생.
       */
      @Override
      public List<AccompanyMemberResponse> getAccompanyMemberList(Long accompanyPostId) {

            List<AccompanyMemberEntity> accompanyMember = accompanyMemberRepository.findAllByAccompanyPost_Id(
                            accompanyPostId);

            return accompanyMember.stream()
                    .map(AccompanyMemberResponse::fromEntity).toList();
      }
}
