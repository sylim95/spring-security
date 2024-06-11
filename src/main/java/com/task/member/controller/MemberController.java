package com.task.member.controller;

import com.task.common.dto.BaseResponse;
import com.task.member.dto.request.MemberDetailRequest;
import com.task.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원관리")
@RequestMapping("")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "승인된 사용자에 한해 회원 가입")
    public BaseResponse<Object> saveMemberInfo(@Valid @RequestBody MemberDetailRequest request) {
        return BaseResponse.success(memberService.saveMemberInfo(request));
    }
}
