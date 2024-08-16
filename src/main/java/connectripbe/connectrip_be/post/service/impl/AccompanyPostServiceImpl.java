package connectripbe.connectrip_be.post.service.impl;

import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.entity.AccompanyPost;
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
    public void createPost(AccompanyPostRequest request, String email) {
        Member member = getMember(email);

        // fixme-noah: custom_url, url_qr_path 보류
        accompanyPostRepository.save(new AccompanyPost(
                member,
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
    public AccompanyPostResponse readPost(Long postId) {
        AccompanyPost accompanyPost = getPost(postId);
        return AccompanyPostResponse.fromEntity(accompanyPost);
    }

    @Override
    @Transactional
    public void updatePost(Long id, AccompanyPostRequest request, String email) {
        AccompanyPost accompanyPost = getPost(id);
        Member member = getMember(email);

        // 작성자 검증
        validatePostOwnership(accompanyPost, member);

        accompanyPost.updateAccompanyPost(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                request.title()
        );
    }

    @Override
    @Transactional
    public void deletePost(Long id, String email) {
        AccompanyPost accompanyPost = getPost(id);
        Member member = getMember(email);

        // 작성자 검증
        validatePostOwnership(accompanyPost, member);

        // 게시글 삭제
        accompanyPostRepository.delete(accompanyPost);
    }

    @Override
    public Page<AccompanyPostResponse> postList(Pageable pageable) {
        return accompanyPostRepository.findAll(pageable)
                .map(AccompanyPostResponse::fromEntity);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    private AccompanyPost getPost(Long postId) {
        return accompanyPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    private void validatePostOwnership(AccompanyPost accompanyPost, Member member) {
        if (!accompanyPost.getMember().getEmail().equals(member.getEmail())) {
            throw new IllegalArgumentException("게시글 수정/삭제 권한이 없습니다.");
        }
    }
}
