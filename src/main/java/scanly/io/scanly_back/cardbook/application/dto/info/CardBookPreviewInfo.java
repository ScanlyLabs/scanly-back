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
    public static CardBookPreviewInfo from(CardBook cardBook, Card card) {
        ProfileSnapshot snapshot = cardBook.getProfileSnapshot();
        boolean isDeleted = card == null;
        return new CardBookPreviewInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                isDeleted ? snapshot.name() : card.getName(),
                isDeleted ? snapshot.title() : card.getTitle(),
                isDeleted ? snapshot.company() : card.getCompany(),
                isDeleted ? snapshot.profileImageUrl() : card.getProfileImageUrl(),
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt()
        );
    }
}
