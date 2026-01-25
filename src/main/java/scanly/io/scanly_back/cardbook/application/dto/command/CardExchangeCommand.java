package scanly.io.scanly_back.cardbook.application.dto.command;

public record CardExchangeCommand(
        String senderId,
        String cardId
) {
}
