package connectripbe.connectrip_be.communitypost.repository;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long> {

    Optional<CommunityPostEntity> findByIdAndDeletedAtIsNull(Long id);

    List<CommunityPostEntity> findAllByDeletedAtIsNull();
}
