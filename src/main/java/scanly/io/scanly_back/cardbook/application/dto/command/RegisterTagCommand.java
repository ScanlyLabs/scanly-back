package scanly.io.scanly_back.cardbook.application.dto.command;

public record RegisterTagCommand(
        String cardBookId,
        String name,
        String memberId
) {
}
