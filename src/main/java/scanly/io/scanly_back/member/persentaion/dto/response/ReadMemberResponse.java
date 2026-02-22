package scanly.io.scanly_back.member.persentaion.dto.response;

import scanly.io.scanly_back.member.application.dto.info.ReadMemberInfo;
import scanly.io.scanly_back.member.domain.MemberStatus;

import java.time.LocalDateTime;

public record ReadMemberResponse(
        String id,
        String loginId,
        String name,
        String email,
        MemberStatus status,
        LocalDateTime createdAt
) {

    public static ReadMemberResponse from(ReadMemberInfo info) {
        return new ReadMemberResponse(
                info.id(),
                info.loginId(),
                info.name(),
                info.email(),
                info.status(),
                info.createdAt()
        );
    }
}
