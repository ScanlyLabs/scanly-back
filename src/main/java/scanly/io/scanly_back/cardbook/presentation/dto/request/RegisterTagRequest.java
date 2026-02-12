package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.RegisterTagCommand;

public record RegisterTagRequest(
        @NotBlank(message = "명함첩 ID는 필수입니다.")
        String cardBookId,

        @NotBlank(message = "태그명은 필수입니다.")
        String name
) {
        public RegisterTagCommand toCommand(String memberId) {
                return new RegisterTagCommand(
                        cardBookId,
                        name,
                        memberId
                );
        }
}
