package connectripbe.connectrip_be.auth.jwt.dto;

import connectripbe.connectrip_be.member.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDto {

      private Long id;
      private String email;
      private String password;
      private String roleType;

      public static CustomUserDto fromEntity(MemberEntity memberEntity) {
            return CustomUserDto.builder()
                    .id(memberEntity.getId())
                    .email(memberEntity.getEmail())
                    .password(memberEntity.getPassword())
                    .roleType(memberEntity.getRoleType().getCode())
                    .build();
      }

}
