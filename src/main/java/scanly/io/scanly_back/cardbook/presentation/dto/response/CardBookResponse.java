package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;

public record CardBookResponse(
        String id,
        String cardId,
        ProfileSnapshot profileSnapshot,
        String groupId,
        String memo,
        boolean isFavorite,
        LocalDateTime createdAt
) {
    public static CardBookResponse from(CardBookInfo cardBookInfo) {
        return new CardBookResponse(
                cardBookInfo.id(),
                cardBookInfo.cardId(),
                cardBookInfo.profileSnapshot(),
                cardBookInfo.groupId(),
                cardBookInfo.memo(),
                cardBookInfo.isFavorite(),
                cardBookInfo.createdAt()
        );
    }
}
