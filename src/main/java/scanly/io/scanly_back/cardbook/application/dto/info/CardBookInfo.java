package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;

public record CardBookInfo(
        String id,
        String cardId,
        String name,
        String title,
        String company,
        String profileImageUrl,
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
                null,
                null,
                null,
                null,
                cardBook.getProfileSnapshot(),
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
    }

    public static CardBookInfo from(CardBook cardBook, Card card) {
        ProfileSnapshot snapshot = cardBook.getProfileSnapshot();
        return new CardBookInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                card != null ? card.getName() : snapshot.name(),
                card != null ? card.getTitle() : snapshot.title(),
                card != null ? card.getCompany() : snapshot.company(),
                card != null ? card.getProfileImageUrl() : snapshot.profileImageUrl(),
                snapshot,
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
    }
}
