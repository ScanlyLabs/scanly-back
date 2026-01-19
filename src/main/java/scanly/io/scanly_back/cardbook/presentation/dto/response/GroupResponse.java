package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;

import java.time.LocalDateTime;

public record GroupResponse(
        String id,
        String name,
        int sortOrder,
        LocalDateTime createdAt
) {

    public static GroupResponse from(GroupInfo groupInfo) {
        return new GroupResponse(
                groupInfo.id(),
                groupInfo.name(),
                groupInfo.sortOrder(),
                groupInfo.createdAt()
        );
    }
}
