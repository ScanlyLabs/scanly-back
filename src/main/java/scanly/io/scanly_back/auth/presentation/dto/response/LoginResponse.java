package scanly.io.scanly_back.auth.presentation.dto.response;

import scanly.io.scanly_back.auth.application.dto.info.LoginInfo;

public record LoginResponse(
        String accessToken,
        String refreshToken,        // 모바일 앱이기 때문에 XSS 공격 없음
        String tokenType
) {

    public static LoginResponse from(LoginInfo loginInfo) {
        return new LoginResponse(
                loginInfo.accessToken(),
                loginInfo.refreshToken(),
                "Bearer"
        );
    }
}
