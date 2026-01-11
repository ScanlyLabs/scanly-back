package scanly.io.scanly_back.member.application.dto;

import scanly.io.scanly_back.member.domain.Member;

public record SignUpInfo(
        String loginId
) {

    public static SignUpInfo from(Member savedMember) {
        return new SignUpInfo(
                savedMember.getLoginId()
        );
    }
}
