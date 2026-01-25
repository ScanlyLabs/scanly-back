package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.Group;

import java.time.LocalDateTime;

public record GroupWithCountInfo(
        String id,
        String name,
        int sortOrder,
        long cardBookCount,
        LocalDateTime createdAt
) {
    public static GroupWithCountInfo of(Group group, long cardBookCount) {
        return new GroupWithCountInfo(
                group.getId(),
                group.getName(),
                group.getSortOrder(),
                cardBookCount,
                group.getCreatedAt()
        );
    }
}
