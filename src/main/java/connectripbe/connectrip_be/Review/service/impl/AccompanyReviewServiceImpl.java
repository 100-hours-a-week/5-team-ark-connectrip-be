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
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyReviewServiceImpl implements AccompanyReviewService {

    private final AccompanyReviewRepository accompanyReviewRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public AccompanyReviewResponse createReview(AccompanyReviewRequest reviewRequest) {
        MemberEntity reviewer = findMemberById(reviewRequest.getReviewerId());
        MemberEntity target = findMemberById(reviewRequest.getTargetId());
        ChatRoomEntity chatRoom = findChatRoomById(reviewRequest.getChatRoomId());

        // Check if the chat room is in FINISH state
        if (chatRoom.getChatRoomType() != ChatRoomType.FINISH) {
            throw new GlobalException(ErrorCode.REVIEW_NOT_ALLOWED);
        }

        // Check if a review already exists
        boolean reviewExists = accompanyReviewRepository.existsByReviewerAndTargetAndChatRoom(reviewer, target, chatRoom);
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

        return AccompanyReviewResponse.fromEntity(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccompanyReviewResponse> getReviewsByChatRoomId(Long chatRoomId) {
        List<AccompanyReviewEntity> reviews = accompanyReviewRepository.findByChatRoomId(chatRoomId);
        return reviews.stream().map(AccompanyReviewResponse::fromEntity).toList();
    }

    private MemberEntity findMemberById(Long memberId) {
        return memberJpaRepository.findById(memberId).orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private ChatRoomEntity findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ACCOMPANY_POST));
    }
}
