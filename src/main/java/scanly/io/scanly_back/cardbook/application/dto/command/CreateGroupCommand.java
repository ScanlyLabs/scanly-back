package scanly.io.scanly_back.cardbook.application.dto.command;

public record CreateGroupCommand(
        String memberId,
        String name
) {
}
