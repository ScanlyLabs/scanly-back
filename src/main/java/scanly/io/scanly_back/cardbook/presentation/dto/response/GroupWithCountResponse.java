package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.GroupWithCountInfo;

import java.time.LocalDateTime;

public record GroupWithCountResponse(
        String id,
        String name,
        int sortOrder,
        long cardBookCount,
        LocalDateTime createdAt
) {

    public static GroupWithCountResponse from(GroupWithCountInfo info) {
        return new GroupWithCountResponse(
                info.id(),
                info.name(),
                info.sortOrder(),
                info.cardBookCount(),
                info.createdAt()
        );
    }
}
