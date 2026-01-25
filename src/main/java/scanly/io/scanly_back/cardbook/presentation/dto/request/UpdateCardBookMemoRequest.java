package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookMemoCommand;

public record UpdateCardBookMemoRequest(
        @NotNull
        String memo
) {
    public UpdateCardBookMemoCommand toCommand(String memberId, String id) {
        return new UpdateCardBookMemoCommand(id, memo, memberId);
    }
}
