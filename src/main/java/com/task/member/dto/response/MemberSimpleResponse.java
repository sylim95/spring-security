package com.task.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSimpleResponse {

    @Schema(description = "사용자 이름")
    private String name;

    public static MemberSimpleResponse of(final String name) {
        return MemberSimpleResponse.builder()
                .name(name).build();
    }
}
