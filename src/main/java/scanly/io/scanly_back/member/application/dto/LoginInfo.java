package scanly.io.scanly_back.member.application.dto;

import scanly.io.scanly_back.member.domain.Member;

public record LoginInfo(
        String loginId
) {

    public static LoginInfo from(Member member) {
        return new LoginInfo(member.getLoginId());
    }
}
