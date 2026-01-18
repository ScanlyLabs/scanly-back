package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.Group;

import java.time.LocalDateTime;

public record GroupInfo(
        String id,
        String name,
        int sortOrder,
        LocalDateTime createdAt
) {
    public static GroupInfo from(Group group) {
        return new GroupInfo(
                group.getId(),
                group.getName(),
                group.getSortOrder(),
                group.getCreatedAt()
        );
    }
}
