package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.SaveCardBookCommand;

public record SaveCardBookRequest(
        @NotBlank(message = "명함 ID는 필수입니다.")
        String cardId,

        String groupId
) {
    public SaveCardBookCommand toCommand(String memberId) {
        return new SaveCardBookCommand(memberId, cardId, groupId);
    }
}
