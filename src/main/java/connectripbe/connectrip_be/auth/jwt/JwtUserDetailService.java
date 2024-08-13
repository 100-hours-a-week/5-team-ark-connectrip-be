package connectripbe.connectrip_be.auth.jwt;



import static connectripbe.connectrip_be.global.exception.type.ErrorCode.USER_NOT_FOUND;

import connectripbe.connectrip_be.auth.jwt.dto.CustomUserDto;
import connectripbe.connectrip_be.global.exception.GlobalException;
import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 사용자를 인증하기 위해 UserDetailsService 인터페이스를 구현한 클래스
 * 주어진 이메일 기반으로 사용자 정보를 로드
 * JWT 기반 인증을 위해 필요한 UserDetails 객체를 반환
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

      private final MemberRepository memberRepository;

      @Override
      public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

            return new JwtUserDetails(CustomUserDto.fromEntity(member));
      }
}
