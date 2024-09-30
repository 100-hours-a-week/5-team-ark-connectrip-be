package connectripbe.connectrip_be.accompany.comment.service.impl;

import connectripbe.connectrip_be.accompany.comment.dto.AccompanyCommentRequest;
import connectripbe.connectrip_be.accompany.comment.dto.AccompanyCommentResponse;
import connectripbe.connectrip_be.accompany.comment.entity.AccompanyCommentEntity;
import connectripbe.connectrip_be.accompany.comment.repository.AccompanyCommentRepository;
import connectripbe.connectrip_be.accompany.comment.service.AccompanyCommentService;
import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.global.util.bucket4j.annotation.RateLimit;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccompanyCommentServiceImpl implements AccompanyCommentService {

    private final AccompanyCommentRepository accompanyCommentRepository;
    private final MemberJpaRepository memberRepository;
    private final AccompanyPostRepository accompanyPostRepository;
    private final NotificationService notificationService; // 주입된 notificationService


    /**
     * 댓글을 생성하는 메서드. 사용자 이메일을 통해 MemberEntity를 조회하고, 게시물 ID를 통해 AccompanyPostEntity를 조회한 후 AccompanyCommentEntity를 생성하여
     * 데이터베이스에 저장
     *
     * @param memberId 댓글 작성자의 아이디
     * @param request  댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @return 생성된 댓글의 정보를 담은 AccompanyCommentResponse 객체
     */
    @Override
    @RateLimit(capacity = 10, refillTokens = 10)
    public AccompanyCommentResponse createComment(Long memberId, AccompanyCommentRequest request) {
        MemberEntity member = getMember(memberId);
        AccompanyPostEntity post = getPost(request.getPostId());

        // 댓글 생성
        AccompanyCommentEntity comment = AccompanyCommentEntity.builder()
                .memberEntity(member)
                .accompanyPostEntity(post)
                .content(request.getContent())
                .build();

        accompanyCommentRepository.save(comment);

        notificationService.sendNotification(post.getMemberEntity().getId(), post, comment.getContent(), member);

        return AccompanyCommentResponse.fromEntity(comment);
    }


    /**
     * 댓글을 수정하는 메서드. 주어진 댓글 ID를 통해 AccompanyCommentEntity를 조회하고, 수정 권한이 있는지 확인한 후 댓글 내용을 업데이트
     *
     * @param memberId  수정하려는 사용자의 아이디
     * @param request   댓글 수정 요청 정보 (수정된 댓글 내용 포함)
     * @param commentId 수정할 댓글의 ID
     * @return 수정된 댓글의 정보를 담은 AccompanyCommentResponse 객체
     */
    @Override
    @Transactional
    public AccompanyCommentResponse updateComment(Long memberId, Long commentId, AccompanyCommentRequest request) {
        AccompanyCommentEntity comment = getComment(commentId);

        // 댓글 작성자와 요청한 사용자가 일치하는지 확인
        validateCommentAuthor(memberId, comment);

        // 댓글 내용 업데이트
        comment.setContent(request.getContent());

        return AccompanyCommentResponse.fromEntity(accompanyCommentRepository.save(comment));
    }

    /**
     * 댓글을 삭제하는 메서드. 주어진 댓글 ID를 통해 AccompanyCommentEntity를 조회한 후, 삭제 권한이 있는지 확인하고 해당 댓글을 데이터베이스에서 삭제
     *
     * @param memberId  삭제하려는 사용자의 아이디
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        AccompanyCommentEntity comment = getComment(commentId);

        // 댓글 작성자와 요청한 사용자가 일치하는지 확인
        validateCommentAuthor(memberId, comment);

        comment.deleteEntity();
    }

    /**
     * 특정 게시물에 달린 모든 댓글을 조회하는 메서드. 주어진 게시물 ID를 통해 해당 게시물에 달린 삭제되지 않은 댓글 목록을 조회한 후, 이를 DTO로 변환하여 반환
     *
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 해당 게시물에 달린 댓글들의 정보를 담은 List<AccompanyCommentResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccompanyCommentResponse> getCommentsByPost(Long postId) {
        List<AccompanyCommentEntity> comments = accompanyCommentRepository.findByAccompanyPostEntity_IdAndDeletedAtIsNull(
                postId);
        return comments.stream()
                .map(AccompanyCommentResponse::fromEntity)
                .toList();
    }

    /**
     * 주어진 댓글 ID로 댓글을 조회하는 메서드. 만약 해당 댓글이 존재하지 않으면 GlobalException 을 발생
     *
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 AccompanyCommentEntity 객체
     */
    private AccompanyCommentEntity getComment(Long commentId) {
        return accompanyCommentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * 주어진 게시물 ID로 게시물을 조회하는 메서드. 만약 해당 게시물이 존재하지 않으면 GlobalException을 발생
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 AccompanyPostEntity 객체
     */
    private AccompanyPostEntity getPost(Long postId) {
        return accompanyPostRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }

    /**
     * 주어진 이메일로 회원을 조회하는 메서드. 만약 해당 이메일로 등록된 회원이 존재하지 않으면 GlobalException을 발생
     *
     * @param memberId 조회할 회원의 아이디
     * @return 조회된 MemberEntity 객체
     */
    private MemberEntity getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }

    /**
     * 댓글 작성자와 요청한 사용자가 일치하는지 확인하는 메서드. 만약 일치하지 않으면 GlobalException을 발생
     *
     * @param memberId 요청한 사용자의 아이디
     * @param comment  조회된 AccompanyCommentEntity 객체
     */
    private void validateCommentAuthor(Long memberId, AccompanyCommentEntity comment) {
        if (!comment.getMemberEntity().getId().equals(memberId)) {
            throw new GlobalException(ErrorCode.WRITE_NOT_YOURSELF);
        }
    }
}
