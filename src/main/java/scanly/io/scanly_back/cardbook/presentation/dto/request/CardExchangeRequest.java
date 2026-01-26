package scanly.io.scanly_back.cardbook.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import scanly.io.scanly_back.cardbook.application.dto.command.CardExchangeCommand;

public record CardExchangeRequest(
        @NotBlank(message = "명함 ID는 필수입니다.")
        String cardId
) {
        public CardExchangeCommand toCommand(String memberId) {
                return new CardExchangeCommand(memberId, cardId);
        }
}
