package scanly.io.scanly_back.member.application.dto.info;

import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberStatus;

import java.time.LocalDateTime;

public record UpdateMemberInfo(
        String id,
        String loginId,
        String name,
        String email,
        MemberStatus status
) {

    public static UpdateMemberInfo from(Member member) {
        return new UpdateMemberInfo(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getEmail(),
                member.getStatus()
        );
    }
}
