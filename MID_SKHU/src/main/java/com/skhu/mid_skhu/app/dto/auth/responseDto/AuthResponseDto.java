package com.skhu.mid_skhu.app.dto.auth.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
}
