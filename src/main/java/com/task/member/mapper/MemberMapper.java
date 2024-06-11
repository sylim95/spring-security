package com.task.member.mapper;

import com.task.member.domain.Member;
import com.task.member.dto.request.MemberDetailRequest;
import com.task.member.dto.response.MemberDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    MemberDetailResponse toDetailDto(Member member);
    Member joinDtoToEntity(MemberDetailRequest request);
}
