package connectripbe.connectrip_be.community.comment.service.impl;

import connectripbe.connectrip_be.community.comment.dto.CommunityCommentRequest;
import connectripbe.connectrip_be.community.comment.dto.CommunityCommentResponse;
import connectripbe.connectrip_be.community.comment.entity.CommunityCommentEntity;
import connectripbe.connectrip_be.community.comment.repository.CommunityCommentRepository;
import connectripbe.connectrip_be.community.comment.service.CommunityCommentService;
import connectripbe.connectrip_be.community.post.entity.CommunityPostEntity;
import connectripbe.connectrip_be.community.post.repository.CommunityPostRepository;
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
public class CommunityCommentServiceImpl implements CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;
    private final MemberJpaRepository memberRepository;
    private final CommunityPostRepository communityPostRepository;
    private final NotificationService notificationService;


    /**
     * 새로운 댓글을 생성하는 메서드. 주어진 사용자의 ID와 요청 정보를 바탕으로 댓글을 생성하고 데이터베이스에 저장합니다.
     *
     * @param memberId 댓글 작성자의 ID
     * @param request  댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @return 생성된 댓글 정보를 담은 CommunityCommentResponse 객체
     */
    @RateLimit(capacity = 10, refillTokens = 10)
    @Override
    public CommunityCommentResponse createComment(Long memberId, CommunityCommentRequest request) {
        MemberEntity member = getMember(memberId);

        CommunityPostEntity post = getPost(request.getPostId());

        CommunityCommentEntity comment = request.toEntity(post, member);
        communityCommentRepository.save(comment);

        // SSE 알림 전송: 게시물 작성자에게 알림을 보냄
        notificationService.sendNotification(post.getMemberEntity().getId(), post, comment.getContent(), member);

        return CommunityCommentResponse.fromEntity(comment);
    }

    /**
     * 댓글을 수정하는 메서드. 주어진 댓글 ID로 댓글을 조회하고, 수정 권한이 있는지 확인한 후 댓글 내용을 업데이트합니다.
     *
     * @param memberId  댓글 수정 요청자의 ID
     * @param commentId 수정할 댓글의 ID
     * @param request   수정된 댓글 내용을 포함한 요청 정보
     * @return 수정된 댓글 정보를 담은 CommunityCommentResponse 객체
     * @throws GlobalException 수정 권한이 없는 경우 예외 발생
     */
    @Override
    @Transactional
    public CommunityCommentResponse updateComment(Long memberId, Long commentId, CommunityCommentRequest request) {
        CommunityCommentEntity comment = communityCommentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));

        if (!communityCommentRepository.existsByIdAndMemberEntity_IdAndDeletedAtIsNull(commentId, memberId)) {
            throw new GlobalException(ErrorCode.WRITE_NOT_YOURSELF);
        }

        comment.updateContent(request.getContent());

        communityCommentRepository.save(comment);

        return CommunityCommentResponse.fromEntity(comment);
    }


    /**
     * 댓글을 삭제하는 메서드. 주어진 댓글 ID로 댓글을 조회하고, 댓글 작성자와 현재 사용자가 일치하는지 확인한 후 삭제 처리합니다.
     *
     * @param memberId  삭제 요청자의 ID
     * @param commentId 삭제할 댓글의 ID
     * @throws GlobalException 댓글이 존재하지 않거나 작성자가 아닌 경우 예외 발생
     */
    @Override
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        CommunityCommentEntity comment = communityCommentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));

        if (!communityCommentRepository.existsByIdAndMemberEntity_IdAndDeletedAtIsNull(commentId, memberId)) {
            throw new GlobalException(ErrorCode.WRITE_NOT_YOURSELF);
        }

        comment.deleteEntity();
        communityCommentRepository.save(comment);
    }

    /**
     * 특정 게시물에 달린 댓글 목록을 조회하는 메서드. 주어진 게시물 ID로 해당 게시물에 달린 삭제되지 않은 댓글들을 조회합니다.
     *
     * @param postId 댓글을 조회할 게시물의 ID
     * @return 조회된 댓글 목록을 담은 List<CommunityCommentResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommunityCommentResponse> getCommentsByPost(Long postId) {
        List<CommunityCommentEntity> comments = communityCommentRepository.findByCommunityPostEntity_IdAndDeletedAtIsNull(
                postId);

        return comments.stream()
                .map(CommunityCommentResponse::fromEntity)
                .toList();
    }

    /**
     * 주어진 게시물 ID로 게시물을 조회하는 메서드. 게시물이 존재하지 않거나 삭제된 경우 예외를 발생시킵니다.
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 CommunityPostEntity 객체
     */
    private CommunityPostEntity getPost(Long postId) {
        return communityPostRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }

    /**
     * 주어진 사용자 ID로 회원 정보를 조회하는 메서드. 회원이 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param memberId 조회할 사용자의 ID
     * @return 조회된 MemberEntity 객체
     */
    private MemberEntity getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
