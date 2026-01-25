package scanly.io.scanly_back.cardbook.application.dto.command;

public record UpdateCardBookGroupCommand(
        String id,
        String groupId,
        String memberId
) {
}
