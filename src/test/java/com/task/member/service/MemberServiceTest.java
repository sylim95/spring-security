package com.task.member.service;

import com.task.error.exception.BusinessException;
import com.task.member.domain.ApproveMember;
import com.task.member.domain.Member;
import com.task.member.dto.request.MemberDetailRequest;
import com.task.member.repository.ApprovedMemberRepository;
import com.task.member.repository.MemberRepository;
import com.task.member.repository.adapter.MemberJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApprovedMemberRepository approvedMemberRepository;

    private PasswordEncoder passwordEncoder;
    private MemberDetailRequest member;
    private Member memberEntity;
    List<ApproveMember> findApproveMemberByName;
    private final String name = "홍길동";
    private final String userId = "gildong";

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        MemberJpaAdapter memberJpaAdapter = new MemberJpaAdapter(memberRepository, approvedMemberRepository, passwordEncoder);
        memberService = new MemberService(memberJpaAdapter, passwordEncoder);

        String password = "1234";
        String regNo = "950000-2222222";
        member = MemberDetailRequest.builder()
                        .userId(userId)
                        .password(password)
                        .name(name)
                        .regNo(regNo)
                        .build();

        memberEntity = Member.builder()
                        .userId(userId)
                        .password(password)
                        .name(name)
                        .regNo(regNo)
                        .build();

        findApproveMemberByName = Collections.singletonList(ApproveMember.builder().name(name).regNo(passwordEncoder.encode(regNo)).build());
    }

    @DisplayName("회원가입 성공")
    @Test
    void singup() {
        // given
        when(memberRepository.findById(userId)).thenReturn(Optional.empty());
        when(approvedMemberRepository.findApproveMemberByName(name)).thenReturn(findApproveMemberByName);
        when(memberRepository.save(any(Member.class))).thenReturn(memberEntity);

        // when
        String memberName = memberService.saveMemberInfo(member).getName();

        // then
        assertThat(memberName).isEqualTo(name);
    }

    @DisplayName("이미 존재하는 아이디 가입 요청")
    @Test
    void singupExistMember() {
        // given
        when(memberRepository.findById(userId)).thenReturn(Optional.of(memberEntity));

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> memberService.saveMemberInfo(member));

        // then
        assertEquals("이미 존재하는 아이디입니다.", exception.getMessage());
    }

    @DisplayName("승인되지 않은 회원 가입 요청")
    @Test
    void singupUnknownMember() {
        // given
        List<ApproveMember> findApproveMemberByName =
                Collections.singletonList(ApproveMember.builder().name(name).regNo(passwordEncoder.encode("921108-1111111")).build());
        when(memberRepository.findById(userId)).thenReturn(Optional.empty());
        when(approvedMemberRepository.findApproveMemberByName(name)).thenReturn(findApproveMemberByName);

        // when
        BusinessException exception = assertThrows(BusinessException.class, () -> memberService.saveMemberInfo(member));

        // then
        assertEquals("승인된 회원만 가입이 가능합니다.", exception.getMessage());
    }
}
