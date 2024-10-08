package connectripbe.connectrip_be.pending_list.service.impl;

import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomMemberService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import connectripbe.connectrip_be.pending_list.dto.PendingListResponse;
import connectripbe.connectrip_be.pending_list.dto.PendingResponse;
import connectripbe.connectrip_be.pending_list.entity.PendingListEntity;
import connectripbe.connectrip_be.pending_list.entity.type.PendingStatus;
import connectripbe.connectrip_be.pending_list.repository.PendingListRepository;
import connectripbe.connectrip_be.pending_list.service.PendingListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PendingListServiceImpl implements PendingListService {


    private final PendingListRepository pendingListRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final AccompanyPostRepository accompanyPostRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMemberService chatRoomMemberService;


    /**
     * 게시물 작성자 또는 방장이 해당 게시물에 신청한 사용자들의 리스트를 조회
     *
     * @param memberId        조회할 사용자의 ID
     * @param accompanyPostId 조회할 게시물의 ID
     * @return List<PendingListResponse> 게시물에 신청한 사용자들의 리스트
     */
    @Override
    public List<PendingListResponse> getPendingList(Long memberId, Long accompanyPostId) {
        MemberEntity member = getMember(memberId);

        ChatRoomEntity chatRoom = chatRoomRepository.findByAccompanyPost_Id(accompanyPostId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHATROOM_NOT_FOUND));

        //현재 사용자가 해당 채팅방 방장(리더)인지 확인
        if (!chatRoom.getCurrentLeader().getMember().equals(member)) {
            throw new GlobalException(ErrorCode.NOT_CHATROOM_LEADER);
        }

        // 채팅방에 속한 멤버들의 리스트 조회
        List<PendingListEntity> pendingList = pendingListRepository.findByAccompanyPost(chatRoom.getAccompanyPost());

        // 상태가 PENDING 인 신청자들의 리스트를 반환
        return pendingList.stream()
                .filter(pending -> pending.getStatus().equals(PendingStatus.PENDING))
                .map(PendingListResponse::fromEntity)
                .toList();
    }

    /**
     * 현재 로그인한 사용자가 특정 동행 게시물에 대해 신청한 상태를 확인
     *
     * @param accompanyPostId 조회할 게시물의 ID
     * @param memberId        현재 로그인한 사용자의 ID
     * @return PendingResponse 사용자의 신청 상태를 반환하는 객체
     * @throws GlobalException 사용자의 신청 상태를 찾을 수 없는 경우 예외 발생
     */
    @Override
    public PendingResponse getMyPendingStatus(Long memberId, Long accompanyPostId) {
        try {
            // 게시물 ID로 AccompanyPostEntity 조회
            AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

            // 게시물 작성자와 현재 로그인한 사용자가 같은지 확인
            if (accompanyPost.getMemberEntity().getId().equals(memberId)) {
                throw new GlobalException(ErrorCode.WRITE_YOURSELF);
            }

            // 이메일로 MemberEntity 조회
            MemberEntity member = getMember(memberId);

            // 게시물과 회원 정보를 바탕으로 PendingListEntity 조회
            PendingListEntity pendingStatus = pendingListRepository.findByAccompanyPostAndMember(
                            accompanyPost, member)
                    .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

            // 조회된 신청 상태를 반환
            return PendingResponse.builder()
                    .status(pendingStatus.getStatus().toString())
                    .build();

        } catch (GlobalException e) {
            if (ErrorCode.PENDING_NOT_FOUND.equals(e.getErrorCode())) {
                return PendingResponse.builder()
                        .status("DEFAULT")
                        .build();
            } else if (ErrorCode.WRITE_YOURSELF.equals(e.getErrorCode())) {
                return PendingResponse.builder()
                        .status("NONE")
                        .build();
            } else {
                throw e;
            }
        }
    }

    /**
     * 사용자가 동행 게시물에 신청합니다.
     *
     * @param accompanyPostId 신청할 게시물의 ID
     * @param memberId        현재 로그인한 사용자의 아이디
     * @return PendingResponse 생성된 신청 상태를 반환하는 객체
     * @throws GlobalException 사용자가 존재하지 않거나 게시물을 찾을 수 없는 경우 예외 발생
     */
    @Override
    public PendingResponse accompanyPending(Long memberId, Long accompanyPostId) {
        // 이메일로 MemberEntity 조회
        MemberEntity member = getMember(memberId);
        // 게시물 ID로 AccompanyPostEntity 조회
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

        try {
            // 게시물 작성자와 현재 로그인한 사용자가 같은지 확인
            if (accompanyPost.getMemberEntity().getId().equals(memberId)) {
                throw new GlobalException(ErrorCode.WRITE_YOURSELF);
            }

            // 이미 신청한 사용자인지 확인
            if (pendingListRepository.existsByMemberAndAccompanyPost(member, accompanyPost)) {
                throw new GlobalException(ErrorCode.PENDING_ALREADY_EXISTS);
            }

            // 새로운 PendingListEntity 생성
            PendingListEntity pendingListEntity = PendingListEntity.builder()
                    .member(member)
                    .accompanyPost(accompanyPost)
                    .status(PendingStatus.PENDING)
                    .build();

            // 생성된 엔티티를 저장
            PendingListEntity saved = pendingListRepository.save(pendingListEntity);

            // 저장된 신청 상태를 반환
            return PendingResponse.builder()
                    .status(saved.getStatus().toString())
                    .build();

        } catch (GlobalException e) {
            if (ErrorCode.PENDING_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 이미 존재하는 신청 상태를 조회
                PendingListEntity existingPending = pendingListRepository.findByAccompanyPostAndMember(accompanyPost,
                                member)
                        .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

                // 상태가 DEFAULT라면 PENDING으로 업데이트
                if (existingPending.getStatus().equals(PendingStatus.DEFAULT)) {
                    existingPending.updateStatus(PendingStatus.PENDING);
                    pendingListRepository.save(existingPending);
                }

                // 현재 상태를 반환
                return PendingResponse.builder()
                        .status(existingPending.getStatus().toString())
                        .build();
            } else {
                throw e;
            }
        }
    }


    /**
     * 사용자의 동행 신청을 수락. - 신청 상태를 ACCEPTE 로 변경하고, 해당 사용자를 채팅방에 추가.
     *
     * @param memberId        수락할 사용자의 ID
     * @param accompanyPostId 신청한 게시물의 ID
     * @return PendingResponse 신청 상태 (ACCEPTED)
     * @throws GlobalException 신청 상태를 찾을 수 없거나, 이미 ACCEPTED 상태이거나, 채팅방을 찾을 수 없는 경우 예외 발생
     */
    @Transactional
    @Override
    public PendingResponse acceptPending(Long memberId, Long accompanyPostId) {
        MemberEntity member = getMember(memberId);
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

        PendingListEntity pending = pendingListRepository.findByAccompanyPostAndMember(accompanyPost, member)
                .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

        if (pending.getStatus() == PendingStatus.ACCEPTED) {
            throw new GlobalException(ErrorCode.PENDING_ALREADY_ACCEPTED);
        } else if (pending.getStatus() != PendingStatus.PENDING) {
            throw new GlobalException(ErrorCode.PENDING_NOT_FOUND);
        }

        pending.updateStatus(PendingStatus.ACCEPTED);
        pendingListRepository.save(pending);

        ChatRoomEntity chatRoom = chatRoomRepository.findByAccompanyPost_Id(accompanyPostId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CHATROOM_NOT_FOUND));

        chatRoomMemberService.jointChatRoom(chatRoom.getId(), memberId);

        return PendingResponse.builder()
                .status(pending.getStatus().toString())
                .build();
    }

    /**
     * 사용자의 동행 신청을 거절합니다. 신청 상태를 REJECTED로 변경합니다.
     *
     * @param memberId        거절할 사용자의 ID
     * @param accompanyPostId 신청한 게시물의 ID
     * @return PendingResponse 신청 상태 (REJECTED)
     * @throws GlobalException 신청 상태를 찾을 수 없거나, 이미 REJECTED 상태인 경우 예외 발생
     */
    @Override
    public PendingResponse rejectPending(Long memberId, Long accompanyPostId) {
        MemberEntity member = getMember(memberId);
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

        PendingListEntity pending = pendingListRepository.findByAccompanyPostAndMember(accompanyPost, member)
                .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

        if (pending.getStatus() == PendingStatus.REJECTED) {
            throw new GlobalException(ErrorCode.PENDING_ALREADY_REJECTED);
        }

        pending.updateStatus(PendingStatus.REJECTED);
        pendingListRepository.save(pending);

        return PendingResponse.builder()
                .status(pending.getStatus().toString())
                .build();
    }


    /**
     * 사용자가 자신이 신청한 동행 신청을 취소 . 사용자가 현재 PENDING 상태인 동행 신청을 취소하고, 상태를 DEFAULT 로 변경. 상태가 PENDING 이 아닌 경우, 예외를 발생.
     *
     * @param memberId        신청을 취소할 사용자의 ID
     * @param accompanyPostId 신청을 취소할 게시물의 ID
     * @return PendingResponse 신청 취소 후의 상태를 반환하는 객체 (DEFAULT 상태)
     * @throws GlobalException 신청 상태를 찾을 수 없거나, 상태가 PENDING 이 아닌 경우 예외 발생
     */
    @Override
    public PendingResponse cancelPending(Long memberId, Long accompanyPostId) {
        MemberEntity member = getMember(memberId);
        AccompanyPostEntity accompanyPost = getAccompanyPost(accompanyPostId);

        PendingListEntity pending = pendingListRepository.findByAccompanyPostAndMember(accompanyPost, member)
                .orElseThrow(() -> new GlobalException(ErrorCode.PENDING_NOT_FOUND));

        // 상태가 PENDING 인 경우에만 취소 가능
        if (!pending.getStatus().equals(PendingStatus.PENDING)) {
            throw new GlobalException(ErrorCode.PENDING_NOT_FOUND);
        }

        pending.updateStatus(PendingStatus.DEFAULT);
        pendingListRepository.save(pending);

        return PendingResponse.builder()
                .status(pending.getStatus().toString())
                .build();
    }


    /**
     * 이메일을 통해 MemberEntity 를 조회합니다.
     *
     * @param memberId 조회할 사용자의 아이디
     * @return MemberEntity 이메일에 해당하는 사용자 엔티티
     * @throws GlobalException 사용자를 찾을 수 없는 경우 예외 발생
     */
    private MemberEntity getMember(Long memberId) {
        return memberJpaRepository.findById(memberId)
                // fixme-noah: 예외 통일 필요
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 게시물 ID를 통해 AccompanyPostEntity 를 조회.
     *
     * @param accompanyPostId 조회할 게시물의 ID
     * @return AccompanyPostEntity 게시물 엔티티
     * @throws GlobalException 게시물을 찾을 수 없는 경우 예외 발생
     */
    private AccompanyPostEntity getAccompanyPost(Long accompanyPostId) {
        return accompanyPostRepository.findById(accompanyPostId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));
    }
}

