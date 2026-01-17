package scanly.io.scanly_back.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.auth.application.dto.command.ReissueCommand;

public record ReissueRequest(
        @NotBlank(message = "Refresh Token은 필수입니다.")
        String refreshToken
) {
    public ReissueCommand toCommand() {
        return new ReissueCommand(refreshToken);
    }
}
