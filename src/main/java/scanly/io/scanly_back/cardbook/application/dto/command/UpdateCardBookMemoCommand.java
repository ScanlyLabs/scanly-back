package scanly.io.scanly_back.cardbook.application.dto.command;

public record UpdateCardBookMemoCommand(
        String id,
        String memo,
        String memberId
) {
}
