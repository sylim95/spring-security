package com.task.health;

import com.task.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Hidden
@RequestMapping("/health")
@Slf4j
public class HealthCheckController {


    @GetMapping(value = "")
    public BaseResponse<Object> healthOk() {
        return BaseResponse.success();
    }
}
