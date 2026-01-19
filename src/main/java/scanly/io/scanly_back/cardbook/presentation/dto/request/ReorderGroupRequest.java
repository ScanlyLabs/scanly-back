package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import scanly.io.scanly_back.cardbook.application.dto.command.ReorderGroupCommand;

import java.util.List;

public record ReorderGroupRequest(
        @NotEmpty(message = "그룹 목록은 필수입니다.")
        @Valid
        List<GroupOrder> groups
) {
    public record GroupOrder(
            @NotBlank(message = "그룹 ID는 필수입니다.")
            String id,

            @Min(value = 0, message = "순서는 0 이상이어야 합니다.")
            int sortOrder
    ) {
    }

    public ReorderGroupCommand toCommand(String memberId) {
        List<ReorderGroupCommand.GroupOrder> commandGroups = groups.stream()
                .map(group -> new ReorderGroupCommand.GroupOrder(group.id(), group.sortOrder()))
                .toList();
        return new ReorderGroupCommand(memberId, commandGroups);
    }
}
