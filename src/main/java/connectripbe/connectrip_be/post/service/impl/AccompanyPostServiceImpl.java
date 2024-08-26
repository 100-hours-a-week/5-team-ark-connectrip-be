package connectripbe.connectrip_be.post.service.impl;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany_status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.exception.MemberNotOwnerException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.post.dto.*;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.exception.DuplicatedCustomUrlException;
import connectripbe.connectrip_be.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.post.service.AccompanyPostService;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyPostServiceImpl implements AccompanyPostService {

    private final AccompanyPostRepository accompanyPostRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;

    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public void createAccompanyPost(Long memberId, CreateAccompanyPostRequest request) {
        MemberEntity memberEntity = findMemberEntity(memberId);

        if (request.customUrl() != null && checkDuplicatedCustomUrl(request.customUrl())) {
            throw new DuplicatedCustomUrlException();
        }

        AccompanyPostEntity post = AccompanyPostEntity.builder()
                .memberEntity(memberEntity)
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .accompanyArea(request.accompanyArea())
                .content(request.content())
                .accompanyArea(request.accompanyArea())
                .urlQrPath("temp")
                .customUrl(request.customUrl())
                .build();

        accompanyPostRepository.save(post);
        accompanyStatusJpaRepository.save(
                new AccompanyStatusEntity(post, AccompanyStatusEnum.PROGRESSING));

        // 채팅방 생성 및 게시물 작성자 채팅방 자동 참여 처리
        chatRoomService.createChatRoom(post.getId(), memberId);

    }

    @Override
    @Transactional(readOnly = true)
    public AccompanyPostResponse readAccompanyPost(long id) {
        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        AccompanyStatusEntity accompanyStatusEntity = accompanyStatusJpaRepository
                .findTopByAccompanyPostEntityOrderByCreatedAtDesc(accompanyPostEntity)
                .orElseThrow(NotFoundAccompanyPostException::new);

        ChatRoomEntity chatRoom = chatRoomRepository.findByAccompanyPost_Id(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        return AccompanyPostResponse.fromEntity(accompanyPostEntity,
                accompanyStatusEntity.getAccompanyStatusEnum().toString(), chatRoom);
    }

    @Override
    public AccompanyPostResponse updateAccompanyPost(Long memberId, long id,
            UpdateAccompanyPostRequest request) {
        MemberEntity memberEntity = findMemberEntity(memberId);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        AccompanyStatusEntity accompanyStatusEntity = accompanyStatusJpaRepository
                .findTopByAccompanyPostEntityOrderByCreatedAtDesc(accompanyPostEntity)
                .orElseThrow(NotFoundAccompanyPostException::new);

        accompanyPostEntity.updateAccompanyPost(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                request.content(),
                request.customUrl()
        );

        ChatRoomEntity chatRoom = chatRoomRepository.findByAccompanyPost_Id(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 수정된 데이터를 응답으로 반환
        return AccompanyPostResponse.fromEntity(accompanyPostEntity, accompanyStatusEntity
                .getAccompanyStatusEnum().toString(), chatRoom);
    }

    @Override
    @Transactional
    public void deleteAccompanyPost(Long memberId, long id) {
        MemberEntity memberEntity = findMemberEntity(memberId);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        accompanyPostEntity.deleteEntity();
    }

    @Override
    public List<AccompanyPostListResponse> accompanyPostList() {
        return accompanyPostRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc().stream()
                .map(AccompanyPostListResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccompanyPostListResponse> searchByQuery(String query) {
        return accompanyPostRepository.findAllByQuery(query).stream()
                .map(AccompanyPostListResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkDuplicatedCustomUrl(String customUrl) {
        return accompanyPostRepository.existsByCustomUrl(customUrl);
    }

    private MemberEntity findMemberEntity(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
    }

    private AccompanyPostEntity findAccompanyPostEntity(long accompanyPostId) {
        return accompanyPostRepository.findByIdAndDeletedAtIsNull(accompanyPostId)
                .orElseThrow(NotFoundAccompanyPostException::new);
    }

    private void validateAccompanyPostOwnership(MemberEntity memberEntity,
            AccompanyPostEntity accompanyPostEntity) {
        if (!memberEntity.getId().equals(accompanyPostEntity.getMemberEntity().getId())) {
            throw new MemberNotOwnerException();
        }
    }
}
