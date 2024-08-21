package connectripbe.connectrip_be.member.service;

import connectripbe.connectrip_be.global.dto.GlobalResponse;
import connectripbe.connectrip_be.member.dto.CheckDuplicateEmailDto;
import connectripbe.connectrip_be.member.dto.CheckDuplicateNicknameDto;
import connectripbe.connectrip_be.member.dto.FirstUpdateMemberRequest;
import connectripbe.connectrip_be.member.dto.MemberHeaderInfoDto;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.exception.DuplicateMemberNicknameException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<CheckDuplicateEmailDto> checkDuplicateEmail(String email) {
        boolean existsByEmail = memberJpaRepository.existsByEmail(email);

        return new GlobalResponse<>(existsByEmail ? "DUPLICATED_EMAIL" : "SUCCESS", new CheckDuplicateEmailDto(existsByEmail));
    }

    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<CheckDuplicateNicknameDto> checkDuplicateNickname(String nickname) {
        boolean existsByNickname = memberJpaRepository.existsByNickname(nickname);

        return new GlobalResponse<>(existsByNickname ? "DUPLICATED_NICKNAME" : "SUCCESS", new CheckDuplicateNicknameDto(existsByNickname));
    }

    /**
     * 헤더에 사용자 프로필 이미지와 닉네임을 전달하기 위한 메서드
     *
     * @author noah(49EHyeon42)
     */
    @Transactional(readOnly = true)
    @Override
    public GlobalResponse<MemberHeaderInfoDto> getMemberHeaderInfo(String email) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);

        return new GlobalResponse<>(memberEntity.getNickname() == null ? "FIRST_LOGIN" : "SUCCESS",
                MemberHeaderInfoDto.fromEntity(memberEntity));
    }

    @Transactional
    @Override
    public GlobalResponse<MemberHeaderInfoDto> getFirstUpdateMemberResponse(String email, FirstUpdateMemberRequest request) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(NotFoundMemberException::new);

        // fixme-noah: 추후 글로벌 response가 정해지면 exception handler로 변경
        if (memberJpaRepository.existsByNickname(request.nickname())) {
            return new GlobalResponse<>("DUPLICATED_NICKNAME", null);
        }

        memberEntity.firstUpdate(request.nickname(), request.birthDate(), request.gender());

        return new GlobalResponse<>(
                "SUCCESS",
                MemberHeaderInfoDto.builder()
                        .memberId(memberEntity.getId())
                        .profileImagePath(memberEntity.getProfileImagePath())
                        .nickname(memberEntity.getNickname())
                        .build());
    }
}
