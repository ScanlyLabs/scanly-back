package scanly.io.scanly_back.member.application.dto.info;

import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberStatus;

import java.time.LocalDateTime;

public record ReadMemberInfo(
        String id,
        String loginId,
        String name,
        String email,
        MemberStatus status,
        LocalDateTime createdAt
) {

    public static ReadMemberInfo from(Member member) {
        return new ReadMemberInfo(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getEmail(),
                member.getStatus(),
                member.getCreatedAt()
        );
    }
}
