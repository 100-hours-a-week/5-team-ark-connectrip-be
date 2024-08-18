package connectripbe.connectrip_be.comment.service.impl;

import static connectripbe.connectrip_be.global.exception.type.ErrorCode.COMMENT_NOT_FOUND;
import static connectripbe.connectrip_be.global.exception.type.ErrorCode.NOT_FOUND_ACCOMPANY_POST;
import static connectripbe.connectrip_be.global.exception.type.ErrorCode.NOT_FOUND_MEMBER;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import connectripbe.connectrip_be.comment.repository.AccompanyCommentRepository;
import connectripbe.connectrip_be.comment.service.AccompanyCommentService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccompanyCommentServiceImpl implements AccompanyCommentService {

    private final AccompanyCommentRepository accompanyCommentRepository;
    private final MemberJpaRepository memberRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    @Override
    @Transactional
    public AccompanyCommentResponse createComment(AccompanyCommentRequest request) {
        MemberEntity member = getMember(request.getMemberId());
        AccompanyPostEntity post = getPost(request.getPostId());

        AccompanyCommentEntity comment = new AccompanyCommentEntity(member, post, request.getContent());
        return AccompanyCommentResponse.fromEntity(accompanyCommentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        AccompanyCommentEntity comment = getComment(commentId);
        accompanyCommentRepository.delete(comment);
    }

    private AccompanyCommentEntity getComment(Long commentId) {
        return accompanyCommentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException(COMMENT_NOT_FOUND));
    }

    private AccompanyPostEntity getPost(Long postId) {
        return accompanyPostRepository.findById(postId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_ACCOMPANY_POST));
    }

    private MemberEntity getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(NOT_FOUND_MEMBER));
    }
}
