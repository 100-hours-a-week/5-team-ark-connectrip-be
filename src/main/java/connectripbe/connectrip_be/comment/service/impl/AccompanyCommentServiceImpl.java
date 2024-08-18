package connectripbe.connectrip_be.comment.service.impl;

import connectripbe.connectrip_be.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.comment.entity.AccompanyCommentEntity;
import connectripbe.connectrip_be.comment.repository.AccompanyCommentRepository;
import connectripbe.connectrip_be.comment.service.AccompanyCommentService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccompanyCommentServiceImpl implements AccompanyCommentService {

    private final AccompanyCommentRepository accompanyCommentRepository;
    private final MemberJpaRepository memberRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    @Override
    @Transactional
    public AccompanyCommentResponse createComment(AccompanyCommentRequest request, String email) {
        MemberEntity member = getMember(email);
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

    @Override
    @Transactional(readOnly = true)
    public List<AccompanyCommentResponse> getCommentsByPost(Long postId) {
        List<AccompanyCommentEntity> comments = accompanyCommentRepository.findByAccompanyPostEntity_Id(postId);
        return comments.stream()
                .map(AccompanyCommentResponse::fromEntity)
                .toList();
    }

    private AccompanyCommentEntity getComment(Long commentId) {
        return accompanyCommentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private AccompanyPostEntity getPost(Long postId) {
        return accompanyPostRepository.findById(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }

    private MemberEntity getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
