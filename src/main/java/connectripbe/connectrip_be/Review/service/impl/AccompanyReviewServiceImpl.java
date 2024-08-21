package connectripbe.connectrip_be.Review.service.impl;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.Review.repository.AccompanyReviewRepository;
import connectripbe.connectrip_be.Review.service.AccompanyReviewService;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccompanyReviewServiceImpl implements AccompanyReviewService {

    private final AccompanyReviewRepository accompanyReviewRepository;
    private final MemberJpaRepository MemberJpaRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public AccompanyReviewResponse createReview(AccompanyReviewRequest reviewRequest) {
        var reviewer = MemberJpaRepository.findById(reviewRequest.getReviewerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reviewer ID"));
        var target = MemberJpaRepository.findById(reviewRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid target ID"));
        var chatRoom = chatRoomRepository.findById(reviewRequest.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        AccompanyReviewEntity review = AccompanyReviewEntity.builder()
                .reviewer(reviewer)
                .target(target)
                .chatRoom(chatRoom)
                .content(reviewRequest.getContent())
                .build();

        accompanyReviewRepository.save(review);

        return convertToDTO(review);
    }

    @Override
    public List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId) {
        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findByChatRoomId(chatRoomId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AccompanyReviewResponse convertToDTO(AccompanyReviewEntity review) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewer().getId())
                .targetId(review.getTarget().getId())
                .chatRoomId(review.getChatRoom().getId())  // 수정된 부분
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .deletedAt(review.getDeletedAt())
                .build();
    }
}
