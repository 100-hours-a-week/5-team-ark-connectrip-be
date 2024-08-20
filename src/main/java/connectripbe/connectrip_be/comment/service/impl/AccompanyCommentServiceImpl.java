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

    /**
     * 댓글을 생성하는 메서드.
     * 사용자 이메일을 통해 MemberEntity를 조회하고, 게시물 ID를 통해 AccompanyPostEntity를 조회한 후
     * AccompanyCommentEntity를 생성하여 데이터베이스에 저장
     *
     * @param request 댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @param email 댓글 작성자의 이메일
     * @return 생성된 댓글의 정보를 담은 AccompanyCommentResponse 객체
     */
    @Override
    @Transactional
    public AccompanyCommentResponse createComment(AccompanyCommentRequest request, String email) {
        MemberEntity member = getMember(email);
        AccompanyPostEntity post = getPost(request.getPostId());

        AccompanyCommentEntity comment = AccompanyCommentEntity.builder()
                .memberEntity(member)
                .accompanyPostEntity(post)
                .content(request.getContent())
                .build();

        return AccompanyCommentResponse.fromEntity(accompanyCommentRepository.save(comment));
    }

    /**
     * 댓글을 수정하는 메서드.
     * 주어진 댓글 ID를 통해 AccompanyCommentEntity를 조회하고,
     * 수정 권한이 있는지 확인한 후 댓글 내용을 업데이트
     *
     * @param request 댓글 수정 요청 정보 (수정된 댓글 내용 포함)
     * @param commentId 수정할 댓글의 ID
     * @param email 수정하려는 사용자의 이메일
     * @return 수정된 댓글의 정보를 담은 AccompanyCommentResponse 객체
     */
    @Override
    @Transactional
    public AccompanyCommentResponse updateComment(AccompanyCommentRequest request, Long commentId, String email) {
        AccompanyCommentEntity comment = getComment(commentId);

        // 댓글 작성자와 요청한 사용자가 일치하는지 확인
        validateCommentAuthor(comment, email);

        // 댓글 내용 업데이트
        comment.setContent(request.getContent());

        return AccompanyCommentResponse.fromEntity(accompanyCommentRepository.save(comment));
    }

    /**
     * 댓글을 삭제하는 메서드.
     * 주어진 댓글 ID를 통해 AccompanyCommentEntity를 조회한 후, 해당 댓글을 데이터베이스에서 삭제
     *
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        AccompanyCommentEntity comment = getComment(commentId);
        accompanyCommentRepository.delete(comment);
    }

    /**
     * 특정 게시물에 달린 모든 댓글을 조회하는 메서드.
     * 주어진 게시물 ID를 통해 해당 게시물에 달린 삭제되지 않은 댓글 목록을 조회한 후, 이를 DTO로 변환하여 반환
     *
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 해당 게시물에 달린 댓글들의 정보를 담은 List<AccompanyCommentResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccompanyCommentResponse> getCommentsByPost(Long postId) {
        List<AccompanyCommentEntity> comments = accompanyCommentRepository.findByAccompanyPostEntity_IdAndDeletedAtIsNull(postId);
        return comments.stream()
                .map(AccompanyCommentResponse::fromEntity)
                .toList();
    }

    /**
     * 주어진 댓글 ID로 댓글을 조회하는 메서드.
     * 만약 해당 댓글이 존재하지 않으면 GlobalException을 발생
     *
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 AccompanyCommentEntity 객체
     */
    private AccompanyCommentEntity getComment(Long commentId) {
        return accompanyCommentRepository.findById(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * 주어진 게시물 ID로 게시물을 조회하는 메서드.
     * 만약 해당 게시물이 존재하지 않으면 GlobalException을 발생
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 AccompanyPostEntity 객체
     */
    private AccompanyPostEntity getPost(Long postId) {
        return accompanyPostRepository.findById(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }

    /**
     * 주어진 이메일로 회원을 조회하는 메서드.
     * 만약 해당 이메일로 등록된 회원이 존재하지 않으면 GlobalException을 발생
     *
     * @param email 조회할 회원의 이메일
     * @return 조회된 MemberEntity 객체
     */
    private MemberEntity getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }

    /**
     * 댓글 작성자와 요청한 사용자가 일치하는지 확인하는 메서드.
     * 만약 일치하지 않으면 GlobalException을 발생
     *
     * @param comment 조회된 AccompanyCommentEntity 객체
     * @param email 요청한 사용자의 이메일
     */
    private void validateCommentAuthor(AccompanyCommentEntity comment, String email) {
        if (!comment.getMemberEntity().getEmail().equals(email)) {
            throw new GlobalException(ErrorCode.WRITE_NOT_YOURSELF);
        }
    }
}
