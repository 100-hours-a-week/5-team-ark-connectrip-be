package connectripbe.connectrip_be.accompany_status.service;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany_status.exception.AlreadyFinishedAccompanyStatusException;
import connectripbe.connectrip_be.accompany_status.exception.NotFoundAccompanyStatusException;
import connectripbe.connectrip_be.accompany_status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.accompany_status.response.AccompanyStatusResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.exception.MemberNotOwnerException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.post.repository.AccompanyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccompanyStatusServiceImpl implements AccompanyStatusService {

    private final AccompanyPostRepository accompanyPostRepository;
    private final AccompanyStatusJpaRepository accompanyStatusJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ChatRoomRepository chatRoomRepository;

    // fixme-noah: 최적화 고민 중
    @Transactional(readOnly = true)
    public AccompanyStatusResponse getAccompanyStatus(long AccompanyPostId) {
        AccompanyPostEntity accompanyPostEntity = accompanyPostRepository.findById(AccompanyPostId)
                .orElseThrow(NotFoundAccompanyPostException::new);

        AccompanyStatusEntity accompanyStatusEntity = accompanyStatusJpaRepository.findTopByAccompanyPostEntityOrderByCreatedAtDesc(
                        accompanyPostEntity)
                .orElseThrow(NotFoundAccompanyStatusException::new);

        return new AccompanyStatusResponse(accompanyPostEntity.getId(), accompanyStatusEntity.getAccompanyStatusEnum());
    }

    // fixme-noah: 코드 수정 고민 중
    public void updateAccompanyStatus(Long memberId, long postId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        AccompanyPostEntity accompanyPostEntity = accompanyPostRepository.findById(postId)
                .orElseThrow(NotFoundAccompanyPostException::new);

        ChatRoomEntity chatRoom = chatRoomRepository.findByAccompanyPost_Id(postId)
                .orElseThrow(NotFoundAccompanyPostException::new);

        validateAccompanyPostOwnership(memberEntity, chatRoom);

        AccompanyStatusEntity accompanyStatusEntity = accompanyStatusJpaRepository.findTopByAccompanyPostEntityOrderByCreatedAtDesc(
                        accompanyPostEntity)
                .orElseThrow(NotFoundAccompanyPostException::new);

        if (accompanyStatusEntity.getAccompanyStatusEnum() == AccompanyStatusEnum.PROGRESSING) {
            accompanyStatusJpaRepository.save(
                    new AccompanyStatusEntity(accompanyPostEntity, AccompanyStatusEnum.CLOSED));
        } else if (accompanyStatusEntity.getAccompanyStatusEnum() == AccompanyStatusEnum.CLOSED) {
            accompanyStatusJpaRepository.save(
                    new AccompanyStatusEntity(accompanyPostEntity, AccompanyStatusEnum.FINISHED));
        } else {
            throw new AlreadyFinishedAccompanyStatusException();
        }
    }

    private void validateAccompanyPostOwnership(MemberEntity memberEntity, ChatRoomEntity chatRoom) {
        if (!memberEntity.getId().equals(chatRoom.getCurrentLeader().getMember().getId())) {
            throw new MemberNotOwnerException();
        }
    }
}
