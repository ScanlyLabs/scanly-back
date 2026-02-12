package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.UpdateTagCommand;

public record UpdateTagRequest(
        @NotBlank(message = "태그명은 필수입니다.")
        String name
) {
    public UpdateTagCommand toCommand(String memberId, String id) {
        return new UpdateTagCommand(id, name, memberId);
    }
}
