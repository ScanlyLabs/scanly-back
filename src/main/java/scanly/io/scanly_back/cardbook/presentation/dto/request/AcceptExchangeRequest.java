package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.AcceptExchangeCommand;

public record AcceptExchangeRequest(
        @NotBlank(message = "교환 요청 ID는 필수입니다.")
        String exchangeId
) {
        public AcceptExchangeCommand toCommand(String memberId) {
                return new AcceptExchangeCommand(exchangeId, memberId);
        }
}
