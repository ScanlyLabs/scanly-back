package scanly.io.scanly_back.cardbook.application.dto.command;

public record UpdateTagCommand(
        String id,
        String name,
        String memberId
) {
}
