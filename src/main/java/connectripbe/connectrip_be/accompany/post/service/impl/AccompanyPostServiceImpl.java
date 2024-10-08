package connectripbe.connectrip_be.accompany.post.service.impl;

import connectripbe.connectrip_be.accompany.post.dto.AccompanyPostListResponse;
import connectripbe.connectrip_be.accompany.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.accompany.post.dto.CreateAccompanyPostRequest;
import connectripbe.connectrip_be.accompany.post.dto.SearchAccompanyPostSummaryResponse;
import connectripbe.connectrip_be.accompany.post.dto.UpdateAccompanyPostRequest;
import connectripbe.connectrip_be.accompany.post.entity.AccompanyPostEntity;
import connectripbe.connectrip_be.accompany.post.exception.NotFoundAccompanyPostException;
import connectripbe.connectrip_be.accompany.post.repository.AccompanyPostRepository;
import connectripbe.connectrip_be.accompany.post.service.AccompanyPostService;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany.status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany.status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.chat.entity.ChatRoomEntity;
import connectripbe.connectrip_be.chat.repository.ChatRoomRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomService;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.global.exception.type.ErrorCode;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.exception.MemberNotOwnerException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.member.repository.MemberJpaRepository;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";


    // Base62 인코딩
    private static String base62Encode(BigInteger value) {
        StringBuilder sb = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            sb.append(BASE62.charAt(value.mod(BigInteger.valueOf(62)).intValue()));
            value = value.divide(BigInteger.valueOf(62));
        }
        return sb.reverse().toString();
    }

    @Override
    @Transactional(readOnly = true)
    public AccompanyPostResponse readAccompanyPost(long id) {
        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        AccompanyStatusEntity accompanyStatusEntity = getAccompanyStatusEntity(accompanyPostEntity);

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

        AccompanyStatusEntity accompanyStatusEntity = getAccompanyStatusEntity(accompanyPostEntity);

        accompanyPostEntity.updateAccompanyPost(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                request.content()
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

        // 동행 게시글 삭제 시 동행 상태를 CLOSED 로 변경
        getAccompanyStatusEntity(accompanyPostEntity).changeAccompanyStatus(AccompanyStatusEnum.CLOSED);

        accompanyPostEntity.deleteEntity();
    }

    private AccompanyStatusEntity getAccompanyStatusEntity(AccompanyPostEntity accompanyPostEntity) {
        return accompanyStatusJpaRepository
                .findTopByAccompanyPostEntityOrderByCreatedAtDesc(accompanyPostEntity)
                .orElseThrow(NotFoundAccompanyPostException::new);
    }

    @Override
    public SearchAccompanyPostSummaryResponse accompanyPostList(int page) {
        PageRequest pageRequest = PageRequest.of(
                page - 1,
                10,
                Sort.by(Direction.DESC, "createdAt"));

        Page<AccompanyPostEntity> accompanyPostEntities = accompanyPostRepository
                .findAllByDeletedAtIsNull(pageRequest);

        return new SearchAccompanyPostSummaryResponse(
                accompanyPostEntities.getTotalElements(),
                accompanyPostEntities.getContent().stream()
                        .map(AccompanyPostListResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SearchAccompanyPostSummaryResponse searchByQuery(String query, int page) {
        PageRequest pageRequest = PageRequest.of(
                page - 1,
                10,
                Sort.by(Direction.DESC, "createdAt"));

        Page<AccompanyPostEntity> accompanyPostEntities =
                accompanyPostRepository.findAllByQueryAndDeletedAtIsNull(query, pageRequest);

        return new SearchAccompanyPostSummaryResponse(
                accompanyPostEntities.getTotalElements(),
                accompanyPostEntities.getContent().stream()
                        .map(AccompanyPostListResponse::fromEntity)
                        .toList()
        );
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

    // 해싱을 통해 고유한 단축 URL 생성 후 Base62 인코딩

    @Override
    @Transactional
    public void createAccompanyPost(Long memberId, CreateAccompanyPostRequest request) {
        MemberEntity memberEntity = findMemberEntity(memberId);

        // 게시글 제목과 생성 일시를 기반으로 단축 URL 생성
        String customUrl = generateUniqueUrl(request.title(), LocalDateTime.now());

        AccompanyPostEntity post = AccompanyPostEntity.builder()
                .memberEntity(memberEntity)
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .accompanyArea(request.accompanyArea())
                .content(request.content())
                .accompanyArea(request.accompanyArea())
                .urlQrPath("temp")
                .customUrl(customUrl)
                .build();

        accompanyPostRepository.save(post);
        accompanyStatusJpaRepository.save(
                new AccompanyStatusEntity(post, AccompanyStatusEnum.PROGRESSING));

        // 채팅방 생성 및 게시물 작성자 채팅방 자동 참여 처리
        chatRoomService.createChatRoom(post, memberEntity);
    }

    private String generateShortUrl(String input) {
        try {
            // SHA-256 해시 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes(StandardCharsets.UTF_8));

            // 해시의 앞 4바이트만 사용 (32비트)
            byte[] shortHash = md.digest();
            BigInteger number = new BigInteger(1, shortHash).shiftRight(224); // 상위 32비트만 사용

            // Base62 인코딩
            return base62Encode(number);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    private String generateUniqueUrl(String title, LocalDateTime createdAt) {
        // 게시글 제목과 생성 시간을 조합하여 입력 문자열 생성
        String input = title + createdAt.toString();
        String shortUrl = generateShortUrl(input);

        // 중복된 URL이 없을 때까지 해시를 사용
        while (checkDuplicatedCustomUrl(shortUrl)) {
            input += "1";  // 중복 시 입력을 변경하여 새로운 해시 생성
            shortUrl = generateShortUrl(input);
        }

        return shortUrl;
    }
}
