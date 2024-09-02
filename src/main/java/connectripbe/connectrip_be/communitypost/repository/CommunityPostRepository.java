package connectripbe.connectrip_be.communitypost.repository;

import connectripbe.connectrip_be.communitypost.entity.CommunityPostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long> {

    // 제목으로 게시글 검색
    List<CommunityPostEntity> findByTitleContaining(String title);

    // 내용으로 게시글 검색
    List<CommunityPostEntity> findByContentContaining(String content);
}
