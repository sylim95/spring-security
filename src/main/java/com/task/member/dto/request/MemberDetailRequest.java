package com.task.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailRequest {

    @Schema(description = "사용자 아이디")
    @NotEmpty(message = "아이디는 비어있을 수 없습니다.")
    private String userId;

    @Schema(description = "비밀번호")
    @NotEmpty(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;

    @Schema(description = "이름")
    @NotEmpty(message = "이름은 비어있을 수 없습니다.")
    private String name;

    @Schema(description = "주민번호")
    @NotEmpty(message = "주민번호는 비어있을 수 없습니다.")
    private String regNo;

    @Schema(hidden = true)
    private String rawRegNo;
}
