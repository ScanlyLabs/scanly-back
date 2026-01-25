package scanly.io.scanly_back.cardbook.application.dto.command;

public record UpdateCardBookFavoriteCommand(
        String id,
        boolean favorite,
        String memberId
) {
}
