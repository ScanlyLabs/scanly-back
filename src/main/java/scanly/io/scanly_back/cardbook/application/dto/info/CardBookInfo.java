package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;

public record CardBookInfo(
        String id,
        String cardId,
        ProfileSnapshot profileSnapshot,
        String groupId,
        String memo,
        boolean isFavorite,
        LocalDateTime createdAt
) {
    public static CardBookInfo from(CardBook cardBook) {
        return new CardBookInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                cardBook.getProfileSnapshot(),
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
    }
}
