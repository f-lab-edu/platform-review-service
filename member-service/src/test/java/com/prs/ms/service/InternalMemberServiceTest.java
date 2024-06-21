package com.prs.ms.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.repository.MemberRepository;
import com.prs.ms.type.MemberRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class InternalMemberServiceTest {


    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    InternalMemberServiceImpl internalMemberService;

    private Member member;

    private static final String username = "testUser";
    private static final String password = "as123as123";

    @Test
    void findMemberInfoTest() {
        // given
        setContextByUsername(username);

        Member member = new Member(username, password, MemberRole.ROLE_USER);
        Member spyMember = spy(member);

        when(spyMember.getId()).thenReturn(1L);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(spyMember));

        // when
        MemberResponseDto memberInfo = internalMemberService.findMemberInfo();

        // then
        Assertions.assertThat(memberInfo.getName()).isEqualTo(username);
        Assertions.assertThat(memberInfo.getMemberId()).isEqualTo(1L);


    }

    @Test
    void findMemberInfoByIdTest() {

        // given
        Member member = new Member(username, "123123", MemberRole.ROLE_USER);
        Member spyMember = spy(member);
        when(spyMember.getId()).thenReturn(1L);
        when(memberRepository.findById(any())).thenReturn(Optional.of(spyMember));

        // when
        MemberResponseDto memberInfo = internalMemberService.findMemberInfoById(1L);

        // then
        Assertions.assertThat(memberInfo.getName()).isEqualTo(username);
        Assertions.assertThat(memberInfo.getMemberId()).isEqualTo(1L);

    }


    @Test
    void findMembersTest() {
        // given
        List<Member> memberList = getMockMemberList(10);
        List<Long> memberIdList = memberList.stream().map(Member::getId).toList();

        when(memberRepository.findByIdIn(any())).thenReturn(memberList);

        // when
        HashMap<Long, MemberResponseDto> members = internalMemberService.findMembers(memberIdList);

        // then
        Assertions.assertThat(members.keySet()).containsOnly(memberIdList.toArray(new Long[0]));
    }

    

    private List<Member> getMockMemberList(Integer count) {
        List<Member> memberList = new ArrayList<>();
        for (int i = 1; i <= count; ++i) {
            Member member = new Member(username + i, password, MemberRole.ROLE_USER);
            Member spyMember = spy(member);
            when(spyMember.getId()).thenReturn((long) i);
            memberList.add(spyMember);
        }
        return memberList;
    }


    private void setContextByUsername(String username) {
        UserDetails userDetails = new User(username, password, new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities()));
    }

}
