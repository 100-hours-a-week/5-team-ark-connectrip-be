package connectripbe.connectrip_be.post.service.impl;

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

    @Override
    public void createAccompanyPost(String memberEmail, AccompanyPostRequest request) {
        Member memberEntity = findMemberEntity(memberEmail);

        // fixme-noah: custom_url, url_qr_path 보류
        accompanyPostRepository.save(new AccompanyPostEntity(
                memberEntity,
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                "temp",
                "temp",
                request.content()));
    }

    @Override
    @Transactional(readOnly = true)
    public AccompanyPostResponse readAccompanyPost(long id) {
        return AccompanyPostResponse.fromEntity(findAccompanyPostEntity(id));
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
                .map(AccompanyPostResponse::fromEntity);
    }

    private Member findMemberEntity(String email) {
        return memberRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
    }

    private AccompanyPostEntity findAccompanyPostEntity(long accompanyPostId) {
        return accompanyPostRepository.findById(accompanyPostId).orElseThrow(NotFoundAccompanyPostException::new);
    }

    private void validateAccompanyPostOwnership(Member member, AccompanyPostEntity accompanyPostEntity) {
        if (!accompanyPostEntity.getMember().getEmail().equals(member.getEmail())) {
            throw new IllegalArgumentException("게시글 수정/삭제 권한이 없습니다.");
        }
    }
}
