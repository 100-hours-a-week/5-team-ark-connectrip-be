package connectripbe.connectrip_be.Review.service.impl;

import connectripbe.connectrip_be.Review.dto.AccompanyReviewRequest;
import connectripbe.connectrip_be.Review.dto.AccompanyReviewResponse;
import connectripbe.connectrip_be.Review.entity.AccompanyReviewEntity;
import connectripbe.connectrip_be.Review.repository.AccompanyReviewRepository;
import connectripbe.connectrip_be.Review.service.AccompanyReviewService;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccompanyReviewServiceImpl implements AccompanyReviewService {

    private final AccompanyReviewRepository accompanyReviewRepository;
    private final MemberJpaRepository MemberJpaRepository;
    private final AccompanyPostRepository accompanyPostRepository;

    @Override
    public AccompanyReviewResponse createReview(AccompanyReviewRequest reviewRequest) {
        var reviewer = MemberJpaRepository.findById(reviewRequest.getReviewerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reviewer ID"));
        var target = MemberJpaRepository.findById(reviewRequest.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid target ID"));
        var accompanyPost = accompanyPostRepository.findById(reviewRequest.getAccompanyPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid accompany post ID"));

        AccompanyReviewEntity review = AccompanyReviewEntity.builder()
                .reviewer(reviewer)
                .target(target)
                .accompanyPost(accompanyPost)
                .content(reviewRequest.getContent())
                .build();

        accompanyReviewRepository.save(review);

        return convertToDTO(review);
    }

    @Override
    public List<AccompanyReviewResponse> getReviewsByAccompanyPostId(Long accompanyPostId) {
        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findByAccompanyPostId(accompanyPostId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AccompanyReviewResponse convertToDTO(AccompanyReviewEntity review) {
        return AccompanyReviewResponse.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewer().getId())
                .targetId(review.getTarget().getId())
                .accompanyPostId(review.getAccompanyPost().getId())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .deletedAt(review.getDeletedAt())
                .build();
    }
}
