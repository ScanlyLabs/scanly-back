package scanly.io.scanly_back.member.persentaion.dto.response;

import scanly.io.scanly_back.member.application.dto.LoginInfo;

public record LoginResponse() {

    public static LoginResponse from(LoginInfo loginInfo) {
        return new LoginResponse();
    }
}
