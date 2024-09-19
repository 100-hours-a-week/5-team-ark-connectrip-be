package connectripbe.connectrip_be.Review.service.impl;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.Review.repository.AccompanyReviewRepository;
import connectripbe.connectrip_be.Review.service.AccompanyReviewService;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.entity.type.ChatRoomType;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
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

        // 채팅방이 FINISH 상태인지 확인
        if (chatRoom.getChatRoomType() != ChatRoomType.FINISH) {
            throw new GlobalException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        // 동일한 리뷰가 이미 존재하는지 확인
        boolean reviewExists = accompanyReviewRepository.existsByReviewerAndTargetAndChatRoom(reviewer, target,
                chatRoom);
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
     * 특정 채팅방에 달린 모든 리뷰를 조회하는 메서드.
     *
     * @param chatRoomId 리뷰를 조회할 채팅방의 ID
     * @return 해당 채팅방에 달린 리뷰들의 정보를 담은 List<AccompanyReviewResponse> 객체
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId) {
        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findByChatRoomId(chatRoomId);

        // 리뷰 수를 계산하고 반환
        return reviews.stream()
                .map(review -> {
                    int reviewCount = accompanyReviewRepository.countByTargetId(review.getTarget().getId());
                    return AccompanyReviewResponse.fromEntity(review, reviewCount);
                })
                .toList();
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
