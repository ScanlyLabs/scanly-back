package scanly.io.scanly_back.member.persentaion.dto.response;

import scanly.io.scanly_back.member.application.dto.info.UpdateMemberInfo;
import scanly.io.scanly_back.member.domain.MemberStatus;

import java.time.LocalDateTime;

public record UpdateMemberResponse(
        String id,
        String loginId,
        String name,
        String email,
        MemberStatus status
) {

    public static UpdateMemberResponse from(UpdateMemberInfo info) {
        return new UpdateMemberResponse(
                info.id(),
                info.loginId(),
                info.name(),
                info.email(),
                info.status()
        );
    }
}
