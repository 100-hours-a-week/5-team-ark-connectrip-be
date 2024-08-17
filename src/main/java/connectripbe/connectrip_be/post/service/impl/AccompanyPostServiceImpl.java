package connectripbe.connectrip_be.post.service.impl;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany_status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.member.exception.MemberNotOwnerException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.post.service.AccompanyPostService;
import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyPostServiceImpl implements AccompanyPostService {

    private final AccompanyPostRepository accompanyPostRepository;
    private final MemberRepository memberRepository;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;

    @Override
    public void createAccompanyPost(String memberEmail, AccompanyPostRequest request) {
        Member memberEntity = findMemberEntity(memberEmail);

        // fixme-noah: custom_url, url_qr_path 보류
        AccompanyPostEntity savedAccompanyPostEntity = accompanyPostRepository.save(new AccompanyPostEntity(
                memberEntity,
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                "temp",
                "temp",
                request.content()));

        // info-noah: status 위치가 모호하다고 느낌, 나중에 백엔드 팀과 상의
        accompanyStatusJpaRepository.save(new AccompanyStatusEntity(savedAccompanyPostEntity, AccompanyStatusEnum.PROGRESSING));
    }

    @Override
    @Transactional(readOnly = true)
    public AccompanyPostResponse readAccompanyPost(long id) {
        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        // fixme-noah: 생성일자가 들어가는가?
        return new AccompanyPostResponse(
                accompanyPostEntity.getId(),
                accompanyPostEntity.getMember().getId(),
                accompanyPostEntity.getTitle(),
                accompanyPostEntity.getStartDate(),
                accompanyPostEntity.getEndDate(),
                accompanyPostEntity.getAccompanyArea(),
                accompanyPostEntity.getCustomUrl(),
                accompanyPostEntity.getUrlQrPath(),
                accompanyPostEntity.getContent()
        );
    }

    @Override
    @Transactional
    public void updateAccompanyPost(String memberEmail, long id, AccompanyPostRequest request) {
        Member memberEntity = findMemberEntity(memberEmail);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        accompanyPostEntity.updateAccompanyPost(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                request.title()
        );
    }

    @Override
    @Transactional
    public void deleteAccompanyPost(String memberEmail, long id) {
        Member memberEntity = findMemberEntity(memberEmail);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        accompanyPostRepository.delete(accompanyPostEntity);
    }

    @Override
    public Page<AccompanyPostResponse> accompanyPostList(Pageable pageable) {
        return accompanyPostRepository.findAll(pageable)
                .map(accompanyPostEntity -> {
                    // fixme-noah: 생성일자가 들어가는가?
                    return new AccompanyPostResponse(
                            accompanyPostEntity.getId(),
                            accompanyPostEntity.getMember().getId(),
                            accompanyPostEntity.getTitle(),
                            accompanyPostEntity.getStartDate(),
                            accompanyPostEntity.getEndDate(),
                            accompanyPostEntity.getAccompanyArea(),
                            accompanyPostEntity.getCustomUrl(),
                            accompanyPostEntity.getUrlQrPath(),
                            accompanyPostEntity.getContent()
                    );
                });
    }

    private Member findMemberEntity(String email) {
        return memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
    }

    private AccompanyPostEntity findAccompanyPostEntity(long accompanyPostId) {
        return accompanyPostRepository.findById(accompanyPostId).orElseThrow(NotFoundAccompanyPostException::new);
    }

    private void validateAccompanyPostOwnership(Member member, AccompanyPostEntity accompanyPostEntity) {
        if (!member.getId().equals(accompanyPostEntity.getMember().getId())) {
            throw new MemberNotOwnerException();
        }
    }
}
