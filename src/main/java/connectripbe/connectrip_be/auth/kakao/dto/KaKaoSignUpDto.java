package connectripbe.connectrip_be.auth.kakao.dto;


import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoSignUpDto {

      @Email
      private String email;

      public static Member toEntity(KaKaoSignUpDto request) {
            return Member.builder()
                    .email(request.getEmail())
                    .roleType(MemberRoleType.USER)
                    .build();
      }

}
