package scanly.io.scanly_back.cardbook.application.dto.command;

public record SaveCardBookCommand(
        String memberId,
        String cardId,
        String groupId
) {
}
