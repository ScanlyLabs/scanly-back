package scanly.io.scanly_back.cardbook.application.dto.command;

import java.util.List;

public record ReorderGroupCommand(
        String memberId,
        List<GroupOrder> groups
) {
    public record GroupOrder(
            String id,
            int sortOrder
    ) {
    }
}
