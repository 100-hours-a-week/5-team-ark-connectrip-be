package connectripbe.connectrip_be.accompany_member.repository;


import connectripbe.connectrip_be.accompany_member.entity.AccompanyMemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccompanyMemberRepository extends JpaRepository<AccompanyMemberEntity, Long> {

      List<AccompanyMemberEntity> findAllByAccompanyPost_Id(Long accompanyPostId);
}
