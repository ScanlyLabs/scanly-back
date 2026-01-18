package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.cardbook.application.dto.command.CreateGroupCommand;

public record CreateGroupRequest(
        @NotBlank(message = "그룹명은 필수입니다.")
        @Size(min = 1, max = 30, message = "그룹명은 1-30자여야 합니다.")
        String name
) {
    public CreateGroupCommand toCommand(String memberId) {
        return new CreateGroupCommand(memberId, name);
    }
}
