package scanly.io.scanly_back.member.persentaion.dto.response;

import scanly.io.scanly_back.member.application.dto.LoginInfo;

public record LoginResponse(
        String id,
        String loginId
) {

    public static LoginResponse from(LoginInfo loginInfo) {
        return new LoginResponse(
                loginInfo.id(),
                loginInfo.loginId()
        );
    }
}
