package connectripbe.connectrip_be.auth.kakao.dto;


import connectripbe.connectrip_be.member.entity.MemberEntity;
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

      public static MemberEntity toEntity(KaKaoSignUpDto request) {
            return MemberEntity.builder()
                    .email(request.getEmail())
                    .roleType(MemberRoleType.USER)
                    .build();
      }

}
