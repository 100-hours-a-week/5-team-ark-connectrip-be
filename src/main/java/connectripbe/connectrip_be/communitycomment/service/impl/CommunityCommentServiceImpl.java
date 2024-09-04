package connectripbe.connectrip_be.communitycomment.service.impl;

import connectripbe.connectrip_be.communitycomment.dto.CommunityCommentRequest;
import connectripbe.connectrip_be.communitycomment.dto.CommunityCommentResponse;
import connectripbe.connectrip_be.communitycomment.entity.CommunityCommentEntity;
import connectripbe.connectrip_be.communitycomment.repository.CommunityCommentRepository;
import connectripbe.connectrip_be.communitycomment.service.CommunityCommentService;
import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import connectripbe.connectrip_be.communitypost.repository.CommunityPostRepository;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
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

    /**
     * 새로운 댓글을 생성하는 메서드. 주어진 사용자의 ID와 요청 정보를 바탕으로 댓글을 생성하고 데이터베이스에 저장합니다.
     *
     * @param memberId 댓글 작성자의 ID
     * @param request  댓글 생성 요청 정보 (게시물 ID, 댓글 내용 포함)
     * @return 생성된 댓글 정보를 담은 CommunityCommentResponse 객체
     */
    @Override
    @Transactional
    public CommunityCommentResponse createComment(Long memberId, CommunityCommentRequest request) {
        MemberEntity member = getMember(memberId);

        CommunityPostEntity post = getPost(request.getPostId());

        CommunityCommentEntity comment = request.toEntity(post, member);

        return CommunityCommentResponse.fromEntity(communityCommentRepository.save(comment));
    }

    /**
     * 댓글을 수정하는 메서드. 주어진 댓글 ID로 댓글을 조회하고, 수정 권한이 있는지 확인한 후 댓글 내용을 업데이트합니다.
     *
     * @param memberId  댓글 수정 요청자의 ID
     * @param commentId 수정할 댓글의 ID
     * @param request   수정된 댓글 내용을 포함한 요청 정보
     * @return 수정된 댓글 정보를 담은 CommunityCommentResponse 객체
     */
    @Override
    @Transactional
    public CommunityCommentResponse updateComment(Long memberId, Long commentId, CommunityCommentRequest request) {
        CommunityCommentEntity comment = getComment(commentId);

        validateCommentAuthor(memberId, comment);

        comment.setContent(request.getContent());

        return CommunityCommentResponse.fromEntity(communityCommentRepository.save(comment));
    }

    /**
     * 댓글을 삭제하는 메서드. 주어진 댓글 ID로 댓글을 조회하고, 삭제 권한이 있는지 확인한 후 댓글을 삭제합니다.
     *
     * @param memberId  삭제 요청자의 ID
     * @param commentId 삭제할 댓글의 ID
     */
    @Override
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        CommunityCommentEntity comment = getComment(commentId);

        validateCommentAuthor(memberId, comment);

        comment.deleteEntity();
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
     * 주어진 댓글 ID로 댓글을 조회하는 메서드. 만약 해당 댓글이 존재하지 않거나 삭제된 경우 예외를 발생시킵니다.
     *
     * @param commentId 조회할 댓글의 ID
     * @return 조회된 CommunityCommentEntity 객체
     */
    private CommunityCommentEntity getComment(Long commentId) {
        return communityCommentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new GlobalException(ErrorCode.COMMENT_NOT_FOUND));
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

    /**
     * 댓글 작성자와 요청자가 일치하는지 확인하는 메서드. 일치하지 않을 경우 예외를 발생시킵니다.
     *
     * @param memberId 요청자의 ID
     * @param comment  조회된 댓글 엔티티
     */
    private void validateCommentAuthor(Long memberId, CommunityCommentEntity comment) {
        if (!comment.getMemberEntity().getId().equals(memberId)) {
            throw new GlobalException(ErrorCode.WRITE_NOT_YOURSELF);
        }
    }
}
