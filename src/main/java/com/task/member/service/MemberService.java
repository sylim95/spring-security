package com.task.member.service;

import com.task.member.dto.request.MemberDetailRequest;
import com.task.member.dto.response.MemberDetailResponse;
import com.task.member.dto.response.MemberSimpleResponse;
import com.task.member.repository.adapter.MemberJpaAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberJpaAdapter memberJpaAdapter;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 찾기
     * @param userId
     * @return MemberDetailResponse
     * */
    @Transactional(readOnly = true)
    public MemberDetailResponse findMember(String userId) {
        return memberJpaAdapter.findMember(userId);
    }


    /**
     * 회원 정보 저장
     * @param request
     * @return MemberSimpleResponse (사용자 이름)
     * */
    @Transactional
    public MemberSimpleResponse saveMemberInfo(MemberDetailRequest request) {
        MemberDetailRequest memberRequest =
                MemberDetailRequest.builder()
                        .userId(request.getUserId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .regNo(passwordEncoder.encode(request.getRegNo()))
                        .rawRegNo(request.getRegNo())
                        .build();
        return memberJpaAdapter.save(memberRequest);
    }
}
