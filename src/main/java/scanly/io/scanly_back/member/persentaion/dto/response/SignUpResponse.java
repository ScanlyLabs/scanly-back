package scanly.io.scanly_back.member.persentaion.dto.response;

import scanly.io.scanly_back.member.application.dto.SignUpInfo;

public record SignUpResponse(
        String id,
        String loginId
) {

    public static SignUpResponse from(SignUpInfo signUpInfo) {
        return new SignUpResponse(
                signUpInfo.id(),
                signUpInfo.loginId()
        );
    }
}
