package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;

public record CardBookPreviewInfo(
        String id,
        String cardId,
        String name,
        String title,
        String company,
        String profileImageUrl,
        String groupId,
        String memo,
        boolean isFavorite,
        LocalDateTime createdAt
) {
    public static CardBookPreviewInfo from(CardBook cardBook) {
        ProfileSnapshot snapshot = cardBook.getProfileSnapshot();
        return new CardBookPreviewInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                snapshot.name(),
                snapshot.title(),
                snapshot.company(),
                snapshot.profileImageUrl(),
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
    }
}
