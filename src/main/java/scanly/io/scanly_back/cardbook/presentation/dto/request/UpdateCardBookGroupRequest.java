package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookGroupCommand;

public record UpdateCardBookGroupRequest(
        String groupId
) {
    public UpdateCardBookGroupCommand toCommand(String memberId, String id) {
        return new UpdateCardBookGroupCommand(id, groupId, memberId);
    }
}
