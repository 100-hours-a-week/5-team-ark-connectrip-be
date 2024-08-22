package connectripbe.connectrip_be.pending_list.repository;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.pending_list.entity.PendingListEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingListRepository extends JpaRepository<PendingListEntity, Long> {

    Optional<PendingListEntity> findByAccompanyPostAndMember(AccompanyPostEntity accompanyPost,
            MemberEntity member);

    boolean existsByMemberAndAccompanyPost(MemberEntity member, AccompanyPostEntity accompanyPost);
}
