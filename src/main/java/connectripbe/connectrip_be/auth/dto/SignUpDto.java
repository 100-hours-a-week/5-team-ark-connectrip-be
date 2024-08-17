package connectripbe.connectrip_be.auth.dto;


import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpDto {

      @Email
      private String email;

      private String password;

      private String nickname;

      private String profileImageUrl;

      public static SignUpDto fromEntity(MemberEntity memberEntity) {
            return SignUpDto.builder()
                    .email(memberEntity.getEmail())
                    .password("password")
                    .nickname(memberEntity.getNickname())
                    .profileImageUrl(memberEntity.getProfileImagePath())
                    .build();
      }

      public static MemberEntity signUpForm(SignUpDto request, String encodedPasswordEncoder) {
            return MemberEntity.builder()
                    .email(request.getEmail())
                    .password(encodedPasswordEncoder)
                    .nickname(request.getNickname())
                    .roleType(MemberRoleType.USER)
                    .loginType(MemberLoginType.EMAIL)
                    .profileImagePath(request.getProfileImageUrl())
                    .build();
      }

}
