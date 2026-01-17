package scanly.io.scanly_back.auth.presentation.dto.response;

import scanly.io.scanly_back.auth.application.dto.info.TokenInfo;

public record TokenResponse(
        String accessToken,
        String refreshToken,        // 모바일 앱이기 때문에 XSS 공격 없음
        String tokenType
) {

    public static TokenResponse from(TokenInfo tokenInfo) {
        return new TokenResponse(
                tokenInfo.accessToken(),
                tokenInfo.refreshToken(),
                "Bearer"
        );
    }
}
