package connectripbe.connectrip_be.auth.dto;


import connectripbe.connectrip_be.member.entity.Member;
import connectripbe.connectrip_be.member.entity.type.MemberLoginType;
import connectripbe.connectrip_be.member.entity.type.MemberRoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

      public static SignUpDto fromEntity(Member member) {
            return SignUpDto.builder()
                    .email(member.getEmail())
                    .password("password")
                    .nickname(member.getNickname())
                    .profileImageUrl(member.getProfileImagePath())
                    .build();
      }

      public static Member signUpForm(SignUpDto request, String encodedPasswordEncoder) {
            return Member.builder()
                    .email(request.getEmail())
                    .password(encodedPasswordEncoder)
                    .nickname(request.getNickname())
                    .roleType(MemberRoleType.USER)
                    .loginType(MemberLoginType.EMAIL)
                    .profileImagePath(request.getProfileImageUrl())
                    .build();
      }

}
