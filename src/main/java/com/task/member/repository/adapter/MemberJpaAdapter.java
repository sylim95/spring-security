package com.task.member.repository.adapter;


import com.task.common.code.ReturnCode;
import com.task.error.exception.BusinessException;
import com.task.member.domain.ApproveMember;
import com.task.member.domain.Member;
import com.task.member.dto.request.MemberDetailRequest;
import com.task.member.dto.response.MemberDetailResponse;
import com.task.member.dto.response.MemberSimpleResponse;
import com.task.member.mapper.MemberMapper;
import com.task.member.repository.ApprovedMemberRepository;
import com.task.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberJpaAdapter {

    private final MemberRepository memberRepository;

    private final ApprovedMemberRepository approvedMemberRepository;

    private final PasswordEncoder passwordEncoder;

    private static final MemberMapper MAPPER = MemberMapper.INSTANCE;


    /**
     * 회원 정보 찾기
     * @param userId
     * @return MemberDetailResponse
     * */
    public MemberDetailResponse findMember(String userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("회원 정보를 찾을 수 없습니다. :: memberName=%s", userId)));

        return MAPPER.toDetailDto(member);
    }

    /**
     * 회원 정보 저장
     * @param request
     * @return MemberSimpleResponse (사용자 이름)
     * */
    public MemberSimpleResponse save(MemberDetailRequest request) {
        String memberName = request.getName();
        checkDuplicatedMember(request.getUserId());
        checkApprovedMember(memberName, request.getRawRegNo());

        // 회원 가입
        memberRepository.save(MAPPER.joinDtoToEntity(request));

        return MemberSimpleResponse.of(memberName);
    }


    /**
     * 회원 존재 여부 확인 후 존재할 경우 에러 발생
     * @param userId
     * @exception 400 error
     * */
    private void checkDuplicatedMember(String userId) {
        // 이미 존재하는 아이디의 경우
        memberRepository.findById(userId).ifPresent(member -> {
            throw BusinessException.of("이미 존재하는 아이디입니다.", ReturnCode.ERROR_400);
        });
    }

    /**
     * 데이터에 저장되어 있지 않은 회원일 경우 에러 발생
     * @param name
     * @param regNo
     * @exception 400 error
     * */
    private void checkApprovedMember(String name, String regNo) {
        // 승인된 회원만 가입 가능
        List<ApproveMember> approvedMembers = approvedMemberRepository.findApproveMemberByName(name);
        boolean isApproved = approvedMembers.stream()
                .anyMatch(approveMember -> passwordEncoder.matches(regNo, approveMember.getRegNo()));

        if(!isApproved) {
            throw BusinessException.of("승인된 회원만 가입이 가능합니다.", ReturnCode.ERROR_400);
        }
    }
}
