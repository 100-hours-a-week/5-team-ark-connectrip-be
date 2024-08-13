package connectripbe.connectrip_be.member.repository;


import connectripbe.connectrip_be.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

      Optional<Member> findByEmail(String email);

      boolean existsByEmail(String email);

      boolean existsByNickname(String nickname);

}
