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
     * 댓글을 생성하는 메서드. 주어진 memberId를 통해 사용자 정보를 조회하고, 요청 정보에서 게시물 ID를 가져와 해당 게시물 정보를 조회한 후, AccompanyCommentRequest를 사용하여
     * 새로운 AccompanyCommentEntity를 생성하고 데이터베이스에 저장합니다.
     *
     * @param memberId 댓글 작성자의 아이디
     * @param request  댓글 생성 요청 정보 (게시물 ID 및 댓글 내용 포함)
     * @return 생성된 댓글 정보를 담고 있는 AccompanyCommentResponse 객체
     * @throws GlobalException 사용자가 존재하지 않거나 게시물이 존재하지 않을 경우 예외 발생
     */
    @Override
    @RateLimit(capacity = 10, refillTokens = 10)
    public AccompanyCommentResponse createComment(Long memberId, AccompanyCommentRequest request) {
        MemberEntity member = getMember(memberId);
        AccompanyPostEntity post = getPost(request.getPostId());

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
     * 댓글을 수정하는 메서드. 주어진 댓글 ID와 작성자 ID로 삭제되지 않은 댓글을 조회하고, 엔티티의 메서드를 사용해 댓글 내용을 업데이트합니다.
     *
     * @param memberId  댓글 작성자의 아이디
     * @param commentId 수정할 댓글의 ID
     * @param request   수정 요청 정보 (수정된 댓글 내용 포함)
     * @return 수정된 댓글 정보를 담고 있는 AccompanyCommentResponse 객체
     * @throws GlobalException 댓글이 존재하지 않거나 작성자가 다를 경우 예외 발생
     */
    @Override
    @Transactional
    public AccompanyCommentResponse updateComment(Long memberId, Long commentId, AccompanyCommentRequest request) {
        AccompanyCommentEntity comment = accompanyCommentRepository.findByIdAndMemberEntity_IdAndDeletedAtIsNull(
                        commentId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.WRITE_NOT_YOURSELF));

        comment.updateContent(request.getContent());

        accompanyCommentRepository.save(comment);

        return AccompanyCommentResponse.fromEntity(comment);
    }

    /**
     * 댓글을 삭제하는 메서드. 주어진 댓글 ID와 작성자 ID로 AccompanyCommentEntity를 조회한 후, 해당 댓글이 본인의 댓글인지 확인하고, 삭제 처리합니다.
     *
     * @param memberId  삭제하려는 사용자의 아이디
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        AccompanyCommentEntity comment = accompanyCommentRepository.findByIdAndMemberEntity_IdAndDeletedAtIsNull(
                        commentId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.WRITE_NOT_YOURSELF));

        comment.deleteEntity();

        accompanyCommentRepository.save(comment);
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
}
