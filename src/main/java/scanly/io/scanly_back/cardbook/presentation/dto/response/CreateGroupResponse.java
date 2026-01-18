package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;

import java.time.LocalDateTime;

public record CreateGroupResponse(
        String id,
        String name,
        int sortOrder,
        LocalDateTime createdAt
) {

    public static CreateGroupResponse from(GroupInfo groupInfo) {
        return new CreateGroupResponse(
                groupInfo.id(),
                groupInfo.name(),
                groupInfo.sortOrder(),
                groupInfo.createdAt()
        );
    }
}
