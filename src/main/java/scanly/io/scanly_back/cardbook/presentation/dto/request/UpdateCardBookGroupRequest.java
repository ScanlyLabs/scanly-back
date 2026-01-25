package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookGroupCommand;

public record UpdateCardBookGroupRequest(
        @NotBlank(message = "그룹 ID는 필수입니다.")
        String groupId
) {
    public UpdateCardBookGroupCommand toCommand(String memberId, String id) {
        return new UpdateCardBookGroupCommand(id, groupId, memberId);
    }
}
