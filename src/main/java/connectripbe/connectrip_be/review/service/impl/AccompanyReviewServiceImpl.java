package connectripbe.connectrip_be.review.service.impl;

import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany.status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.review.dto.AccompanyReviewDto;
import connectripbe.connectrip_be.review.dto.AccompanyReviewListResponse;
import connectripbe.connectrip_be.review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.review.dto.AccompanyReviewSummaryResponse;
import connectripbe.connectrip_be.review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.review.repository.AccompanyReviewRepository;
import connectripbe.connectrip_be.review.service.AccompanyReviewService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyReviewServiceImpl implements AccompanyReviewService {

    private final AccompanyReviewRepository accompanyReviewRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;

    /**
     * 리뷰를 생성하는 메서드.
     *
     * @param reviewRequest 리뷰 생성 요청 정보
     * @return 생성된 리뷰의 정보를 담은 AccompanyReviewResponse 객체
     */
    @Override
    public AccompanyReviewResponse createReview(Long chatRoomId, Long memberId, AccompanyReviewRequest reviewRequest) {
        MemberEntity reviewer = findMemberById(memberId);
        MemberEntity target = findMemberById(reviewRequest.getTargetId());
        ChatRoomEntity chatRoom = findChatRoomById(chatRoomId);

        // AccompanyStatusEntity를 쿼리로 가져와 FINISHED 상태 확인
        AccompanyStatusEntity accompanyStatus = accompanyStatusJpaRepository
                .findTopByAccompanyPostEntityOrderByCreatedAtDesc(chatRoom.getAccompanyPost())
                .orElseThrow(() -> new GlobalException(ErrorCode.REVIEW_NOT_ALLOWED));

        // AccompanyStatusEnum이 FINISHED 상태인지 확인
        if (accompanyStatus.getAccompanyStatusEnum() != AccompanyStatusEnum.FINISHED) {
            throw new GlobalException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        // 동일한 리뷰가 이미 존재하는지 확인
        boolean reviewExists = accompanyReviewRepository.existsByReviewerIdAndTargetIdAndChatRoomId(
                reviewer.getId(), target.getId(), chatRoom.getId());

        if (reviewExists) {
            throw new GlobalException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        AccompanyReviewEntity review = AccompanyReviewEntity.builder()
                .reviewer(reviewer)
                .target(target)
                .chatRoom(chatRoom)
                .content(reviewRequest.getContent())
                .build();

        accompanyReviewRepository.save(review);

        // 리뷰 수를 계산하고 반환
        int reviewCount = accompanyReviewRepository.countByTargetId(target.getId());
        return AccompanyReviewResponse.fromEntity(review, reviewCount);
    }


    /**
     * 특정 채팅방에 달린 모든 리뷰를 조회하고, 각 리뷰에 대한 정보를 AccompanyReviewResponse로 변환하는 메서드.
     *
     * @param chatRoomId 리뷰를 조회할 채팅방의 ID
     * @return 해당 채팅방에 달린 리뷰들의 정보를 담은 List<AccompanyReviewResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId) {
        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findByChatRoomId(chatRoomId);

        return reviews.stream()
                .map(this::convertToAccompanyReviewResponse)
                .toList();
    }

    @Override
    public AccompanyReviewSummaryResponse getReviewSummary(
            Long chatRoomId,
            Long reviewerId,
            Long revieweeId) {
        AccompanyReviewEntity accompanyReviewEntity = accompanyReviewRepository.findAccompanyReviewByChatRoomIdAndReviewerIdAndRevieweeId(
                        chatRoomId,
                        reviewerId,
                        revieweeId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_REVIEW));

        return new AccompanyReviewSummaryResponse(
                accompanyReviewEntity.getTarget().getNickname(),
                accompanyReviewEntity.getContent(),
                formatToUTC(accompanyReviewEntity.getCreatedAt()));
    }

    // todo-noah: 추후 UTC 관련 utils로 분리
    private final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private String formatToUTC(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
                .format(UTC_FORMATTER); // 형식에 맞춰 반환
    }

    /**
     * 특정 회원이 받은 모든 리뷰를 조회하고, 각 리뷰를 AccompanyReviewResponse로 변환하여 반환하는 메서드.
     *
     * @param memberId 리뷰 대상이 되는 회원 ID
     * @return 해당 회원이 받은 모든 리뷰 목록과 리뷰 대상자가 받은 전체 리뷰 수를 포함한 리스트
     * @
     */
    @Override
    public AccompanyReviewListResponse getAllReviews(Long memberId) {
        // 전체 리뷰 목록을 조회
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findAllByTargetIdOrderByCreatedAtDesc(memberId);

        List<AccompanyReviewDto> AccompanyReviewDto = reviews.stream()
                .map(accompanyReviewEntity -> new AccompanyReviewDto(
                        accompanyReviewEntity.getId(),
                        accompanyReviewEntity.getReviewer().getId(),
                        accompanyReviewEntity.getReviewer().getNickname(),
                        accompanyReviewEntity.getReviewer().getProfileImagePath(),
                        accompanyReviewEntity.getContent(),
                        formatToUTC(accompanyReviewEntity.getCreatedAt())
                ))
                .toList();

        return new AccompanyReviewListResponse(
                memberId,
                memberEntity.getNickname(),
                reviews.size(),
                AccompanyReviewDto
        );
    }

    /**
     * AccompanyReviewEntity 객체를 AccompanyReviewResponse로 변환하고 리뷰 개수를 포함시키는 메서드.
     *
     * @param review AccompanyReviewEntity 객체
     * @return 변환된 AccompanyReviewResponse 객체
     */
    private AccompanyReviewResponse convertToAccompanyReviewResponse(AccompanyReviewEntity review) {
        int reviewCount = accompanyReviewRepository.countByTargetId(review.getTarget().getId());
        return AccompanyReviewResponse.fromEntity(review, reviewCount);
    }


    /**
     * 주어진 회원 ID로 회원을 조회하는 메서드. 만약 해당 회원이 존재하지 않으면 GlobalException을 발생시킵니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 조회된 MemberEntity 객체
     */
    private MemberEntity findMemberById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }

    /**
     * 주어진 채팅방 ID로 채팅방을 조회하는 메서드. 만약 해당 채팅방이 존재하지 않으면 GlobalException을 발생시킵니다.
     *
     * @param chatRoomId 조회할 채팅방의 ID
     * @return 조회된 ChatRoomEntity 객체
     */
    private ChatRoomEntity findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }

}
