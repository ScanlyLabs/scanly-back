package scanly.io.scanly_back.cardbook.application.dto.command;

public record RenameGroupCommand(
        String id,
        String name
) {
}
