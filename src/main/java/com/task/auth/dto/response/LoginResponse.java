package com.task.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(String accessToken) {
}
