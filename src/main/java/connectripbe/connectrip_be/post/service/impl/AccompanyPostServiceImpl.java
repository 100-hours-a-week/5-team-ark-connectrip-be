package connectripbe.connectrip_be.post.service.impl;

import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEntity;
import connectripbe.connectrip_be.accompany_status.entity.AccompanyStatusEnum;
import connectripbe.connectrip_be.accompany_status.repository.AccompanyStatusJpaRepository;
import connectripbe.connectrip_be.member.exception.MemberNotOwnerException;
import connectripbe.connectrip_be.member.exception.NotFoundMemberException;
import connectripbe.connectrip_be.post.dto.AccompanyPostListResponse;
import connectripbe.connectrip_be.post.dto.AccompanyPostRequest;
import connectripbe.connectrip_be.post.dto.AccompanyPostResponse;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
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

    @Override
    public AccompanyPostResponse createAccompanyPost(String memberEmail, AccompanyPostRequest request) {
        MemberEntity memberEntity = findMemberEntity(memberEmail);

//        AccompanyPostEntity savedAccompanyPostEntity = accompanyPostRepository.save(new AccompanyPostEntity(
//                memberEntity,
//                request.title(),
//                request.startDate(),
//                request.endDate(),
//                request.accompanyArea(),
//                "temp",
//                "temp",
//                request.content()));

        AccompanyPostEntity post = AccompanyPostEntity.builder()
                .memberEntity(memberEntity)
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .accompanyArea(request.accompanyArea())
                .content(request.content())
                .accompanyArea(request.accompanyArea())
                .urlQrPath("temp")
                .customUrl("temp")
                .requestStatus("DEFAULT")
                .build();

        accompanyPostRepository.save(post);
        accompanyStatusJpaRepository.save(new AccompanyStatusEntity(post, AccompanyStatusEnum.PROGRESSING));
        //accompanyStatusJpaRepository.save(new AccompanyStatusEntity(savedAccompanyPostEntity, AccompanyStatusEnum.PROGRESSING));

        // 생성된 데이터를 응답으로 반환
        return AccompanyPostResponse.fromEntity(post);
    }

    @Override
    @Transactional(readOnly = true)
    public AccompanyPostResponse readAccompanyPost(long id) {
        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        // 로그 추가
        System.out.println("AccompanyPostEntity: " + accompanyPostEntity);

        return AccompanyPostResponse.fromEntity(accompanyPostEntity);
    }

    @Override
    public AccompanyPostResponse updateAccompanyPost(String memberEmail, long id, AccompanyPostRequest request) {
        MemberEntity memberEntity = findMemberEntity(memberEmail);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        accompanyPostEntity.updateAccompanyPost(
                request.title(),
                request.startDate(),
                request.endDate(),
                request.accompanyArea(),
                request.content()
        );

        // 수정된 데이터를 응답으로 반환
        return AccompanyPostResponse.fromEntity(accompanyPostEntity);
    }

    @Override
    @Transactional
    public void deleteAccompanyPost(String memberEmail, long id) {
        MemberEntity memberEntity = findMemberEntity(memberEmail);

        AccompanyPostEntity accompanyPostEntity = findAccompanyPostEntity(id);

        validateAccompanyPostOwnership(memberEntity, accompanyPostEntity);

        accompanyPostRepository.delete(accompanyPostEntity);
    }

    @Override
    public List<AccompanyPostListResponse> accompanyPostList() {
        List<AccompanyPostEntity> all =  accompanyPostRepository.findAll();
        return all.stream().map(AccompanyPostListResponse::fromEntity).toList();
    }

    private MemberEntity findMemberEntity(String email) {
        return memberJpaRepository.findByEmail(email).orElseThrow(NotFoundMemberException::new);
    }

    private AccompanyPostEntity findAccompanyPostEntity(long accompanyPostId) {
        return accompanyPostRepository.findById(accompanyPostId).orElseThrow(NotFoundAccompanyPostException::new);
    }

    private void validateAccompanyPostOwnership(MemberEntity memberEntity, AccompanyPostEntity accompanyPostEntity) {
        if (!memberEntity.getId().equals(accompanyPostEntity.getMemberEntity().getId())) {
            throw new MemberNotOwnerException();
        }
    }
}
