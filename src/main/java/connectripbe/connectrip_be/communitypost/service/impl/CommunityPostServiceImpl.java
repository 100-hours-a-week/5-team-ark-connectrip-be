package connectripbe.connectrip_be.communitypost.service.impl;

import connectripbe.connectrip_be.communitypost.dto.CommunityPostResponse;
import connectripbe.connectrip_be.communitypost.dto.CreateCommunityPostRequest;
import connectripbe.connectrip_be.communitypost.dto.UpdateCommunityPostRequest;
import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import connectripbe.connectrip_be.communitypost.repository.CommunityPostRepository;
import connectripbe.connectrip_be.communitypost.service.CommunityPostService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityPostServiceImpl implements CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final MemberJpaRepository memberJpaRepository;

    /**
     * 게시글을 생성하는 메서드. 사용자 ID를 통해 MemberEntity를 조회하고, 받은 요청 데이터를 사용하여 CommunityPostEntity를 생성하여 데이터베이스에 저장합니다.
     *
     * @param request  게시글 생성 요청 정보 (제목, 내용 포함)
     * @param memberId 게시글 작성자의 ID
     * @return 생성된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @Override
    public CommunityPostResponse createPost(CreateCommunityPostRequest request, Long memberId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

        CommunityPostEntity post = CommunityPostEntity.builder()
                .memberEntity(memberEntity)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        communityPostRepository.save(post);

        return CommunityPostResponse.fromEntity(post);
    }

    /**
     * 게시글을 수정하는 메서드. 주어진 게시글 ID를 통해 CommunityPostEntity를 조회하고, 수정 권한이 있는지 확인한 후 게시글 내용을 업데이트합니다.
     *
     * @param memberId 수정하려는 사용자의 ID
     * @param postId   수정할 게시글의 ID
     * @param request  게시글 수정 요청 정보 (수정된 제목, 내용 포함)
     * @return 수정된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @Override
    public CommunityPostResponse updatePost(Long memberId, Long postId, UpdateCommunityPostRequest request) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

        CommunityPostEntity postEntity = communityPostRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        // 게시글 작성자와 요청한 사용자가 일치하는지 확인
        validatePostOwnership(memberEntity, postEntity);

        // 게시글 내용 업데이트
        postEntity = CommunityPostEntity.builder()
                .id(postEntity.getId())  // 기존 ID 유지
                .memberEntity(memberEntity)  // 기존 작성자 유지
                .title(request.getTitle())
                .content(request.getContent())
                .deletedAt(postEntity.getDeletedAt())  // 기존 삭제 상태 유지
                .build();

        communityPostRepository.save(postEntity);

        return CommunityPostResponse.fromEntity(postEntity);
    }

    /**
     * 게시글을 삭제하는 메서드. 주어진 게시글 ID를 통해 CommunityPostEntity를 조회한 후, 삭제 권한이 있는지 확인하고 해당 게시글을 소프트 딜리트 처리합니다.
     *
     * @param memberId 삭제하려는 사용자의 ID
     * @param postId   삭제할 게시글의 ID
     */
    @Override
    public void deletePost(Long memberId, Long postId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

        CommunityPostEntity postEntity = communityPostRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        // 게시글 작성자와 요청한 사용자가 일치하는지 확인
        validatePostOwnership(memberEntity, postEntity);

        // 소프트 딜리트 처리: deletedAt 필드를 현재 시간으로 설정
        postEntity = CommunityPostEntity.builder()
                .id(postEntity.getId())
                .memberEntity(postEntity.getMemberEntity())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .deletedAt(LocalDateTime.now())  // 소프트 삭제 처리
                .build();

        communityPostRepository.save(postEntity);
    }

    /**
     * 특정 게시글을 조회하는 메서드. 게시글 ID를 통해 게시글을 조회하고, 존재하지 않거나 삭제된 경우 예외를 발생시킵니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글의 정보를 담은 CommunityPostResponse 객체
     */
    @Override
    public CommunityPostResponse readPost(Long postId) {
        CommunityPostEntity postEntity = communityPostRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        return CommunityPostResponse.fromEntity(postEntity);
    }

    /**
     * 모든 게시글을 조회하는 메서드. 삭제되지 않은 모든 게시글을 조회하여 반환합니다.
     *
     * @return 모든 게시글의 정보를 담은 List<CommunityPostResponse> 객체
     */
    @Override
    public List<CommunityPostResponse> getAllPosts() {
        List<CommunityPostEntity> posts = communityPostRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
        return posts.stream()
                .map(CommunityPostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 작성자와 요청한 사용자가 일치하는지 확인하는 메서드. 만약 일치하지 않으면 GlobalException을 발생시킵니다.
     *
     * @param memberEntity 요청한 사용자의 MemberEntity 객체
     * @param postEntity   조회된 CommunityPostEntity 객체
     */
    private void validatePostOwnership(MemberEntity memberEntity, CommunityPostEntity postEntity) {
        if (!postEntity.getMemberEntity().getId().equals(memberEntity.getId())) {
            throw new GlobalException(ErrorCode.MEMBER_NOT_OWNER_EXCEPTION);
        }
    }
}
