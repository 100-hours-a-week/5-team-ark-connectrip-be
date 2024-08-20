package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.member.dto.MemberHeaderInfoResponse;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    /**
     * 헤더에 사용자 프로필 이미지와 닉네임을 전달하기 위한 메서드
     *
     * @author noah(49EHyeon42)
     */
    @Override
    public MemberHeaderInfoResponse getMemberHeaderInfo(String email) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);

        return MemberHeaderInfoResponse.fromEntity(memberEntity);
    }
}
