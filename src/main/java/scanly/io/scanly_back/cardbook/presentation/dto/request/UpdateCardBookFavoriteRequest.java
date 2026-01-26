package scanly.io.scanly_back.cardbook.presentation.dto.request;

import scanly.io.scanly_back.cardbook.application.dto.command.UpdateCardBookFavoriteCommand;

public record UpdateCardBookFavoriteRequest(
        boolean favorite
) {
    public UpdateCardBookFavoriteCommand toCommand(String memberId, String id) {
        return new UpdateCardBookFavoriteCommand(id, favorite, memberId);
    }
}
