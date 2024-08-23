package connectripbe.connectrip_be.pending_list.service.impl;

import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;
import connectripbe.connectrip_be.pending_list.entity.PendingListEntity;
import connectripbe.connectrip_be.pending_list.entity.type.PendingStatus;
import connectripbe.connectrip_be.pending_list.repository.PendingListRepository;
import connectripbe.connectrip_be.pending_list.service.PendingListService;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PendingListServiceImpl implements PendingListService {

    // 의존성 주입된 레포지토리들
    private final PendingListRepository pendingListRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    /**
     * 현재 로그인한 사용자가 특정 동행 게시물에 대해 신청한 상태를 확인
     *
     * @param accompanyPostId 조회할 게시물의 ID
     * @param email 현재 로그인한 사용자의 이메일
     * @return PendingListResponse 사용자의 신청 상태를 반환하는 객체
     * @throws GlobalException 사용자의 신청 상태를 찾을 수 없는 경우 예외 발생
     */
    @Override
    public PendingListResponse getMyPendingStatus(Long accompanyPostId, String email) {
        // 게시물 ID로 AccompanyPostEntity 조회
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);
        // 이메일로 MemberEntity 조회
        MemberEntity member = getMember(email);

        // 게시물과 회원 정보를 바탕으로 PendingListEntity 조회
        PendingListEntity pendingStatus = pendingListRepository.findByAccompanyPostAndMember(
                        accompanyPost, member)
                .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

        // 조회된 신청 상태를 반환
        return PendingListResponse.builder()
                .status(pendingStatus.getStatus())
                .build();
    }


    /**
     * 현재 로그인한 사용자가 특정 동행 게시물에 대해 새로운 동행 신청을 생성.
     *
     * @param accompanyPostId 신청할 게시물의 ID
     * @param email 현재 로그인한 사용자의 이메일
     * @return PendingListResponse 생성된 신청 상태를 반환하는 객체
     * @throws GlobalException 사용자가 존재하지 않거나 게시물을 찾을 수 없는 경우 예외 발생
     */
    @Override
    public PendingListResponse accompanyPending(Long accompanyPostId, String email) {
        // 이메일로 MemberEntity 조회
        MemberEntity member = getMember(email);
        // 게시물 ID로 AccompanyPostEntity 조회
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

        // 이미 신청한 사용자인지 확인
        if (pendingListRepository.existsByMemberAndAccompanyPost(member, accompanyPost)) {
            throw new GlobalException(ErrorCode.PENDING_ALREADY_EXISTS);
        }

        // 새로운 PendingListEntity 생성
        PendingListEntity pendingListEntity = PendingListEntity.builder()
                .member(member)
                .accompanyPost(accompanyPost)
                .status(PendingStatus.PENDING)
                .build();

        // 생성된 엔티티를 저장
        PendingListEntity saved = pendingListRepository.save(pendingListEntity);

        // 저장된 신청 상태를 반환
        return PendingListResponse.builder()
                .status(saved.getStatus())
                .build();
    }

    /**
     * 이메일을 통해 MemberEntity 를 조회합니다.
     *
     * @param email 조회할 사용자의 이메일
     * @return MemberEntity 이메일에 해당하는 사용자 엔티티
     * @throws GlobalException 사용자를 찾을 수 없는 경우 예외 발생
     */
    private MemberEntity getMember(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 게시물 ID를 통해 AccompanyPostEntity 를 조회.
     *
     * @param accompanyPostId 조회할 게시물의 ID
     * @return AccompanyPostEntity 게시물 엔티티
     * @throws GlobalException 게시물을 찾을 수 없는 경우 예외 발생
     */
    private AccompanyPostEntity getAccompanyPost(Long accompanyPostId) {
        return accompanyPostRepository.findById(accompanyPostId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));
    }
}