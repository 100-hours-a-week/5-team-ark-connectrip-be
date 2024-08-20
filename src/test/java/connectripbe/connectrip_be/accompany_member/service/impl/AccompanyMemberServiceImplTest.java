package connectripbe.connectrip_be.accompany_member.service.impl;

import connectripbe.connectrip_be.accompany_member.dto.AccompanyMemberResponse;
import connectripbe.connectrip_be.accompany_member.entity.AccompanyMemberEntity;
import connectripbe.connectrip_be.accompany_member.entity.type.AccompanyMemberStatus;
import connectripbe.connectrip_be.accompany_member.repository.AccompanyMemberRepository;
import connectripbe.connectrip_be.member.entity.MemberEntity;
import connectripbe.connectrip_be.post.entity.AccompanyPostEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AccompanyMemberServiceImplTest {

      @Mock
      private AccompanyMemberRepository accompanyMemberRepository;

      @InjectMocks
      private AccompanyMemberServiceImpl accompanyMemberService;

      @BeforeEach
      void setUp() {
            MockitoAnnotations.openMocks(this);
      }

      @Test
      void testGetAccompanyMemberList() {
            // Given
            Long accompanyPostId = 1L;
            MemberEntity member1 = MemberEntity.builder()
                    .id(1L)
                    .nickname("nickname1")
                    .profileImagePath("path/to/profile1")
                    .build();
            MemberEntity member2 = MemberEntity.builder()
                    .id(2L)
                    .nickname("nickname2")
                    .profileImagePath("path/to/profile2")
                    .build();

            AccompanyPostEntity accompanyPost = AccompanyPostEntity.builder()
                    .id(accompanyPostId)
                    .build();

            AccompanyMemberEntity accompanyMember1 = AccompanyMemberEntity.builder()
                    .id(1L)
                    .member(member1)
                    .accompanyPost(accompanyPost)
                    .status(AccompanyMemberStatus.ACTIVE)
                    .build();

            AccompanyMemberEntity accompanyMember2 = AccompanyMemberEntity.builder()
                    .id(2L)
                    .member(member2)
                    .accompanyPost(accompanyPost)
                    .status(AccompanyMemberStatus.ACTIVE)
                    .build();

            List<AccompanyMemberEntity> accompanyMemberList = Arrays.asList(accompanyMember1, accompanyMember2);

            when(accompanyMemberRepository.findAllByAccompanyPost_Id(accompanyPostId)).thenReturn(accompanyMemberList);

            // When
            List<AccompanyMemberResponse> result = accompanyMemberService.getAccompanyMemberList(accompanyPostId);

            // Then
            assertEquals(2, result.size());
            assertEquals("nickname1", result.get(0).nickname());
            assertEquals("nickname2", result.get(1).nickname());
      }


}