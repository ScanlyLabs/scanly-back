package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;
import java.util.List;

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
        LocalDateTime createdAt,
        List<Tag> tagList
) {

    public static CardBookInfo from(CardBook cardBook, List<Tag> tagList) {
        ProfileSnapshot snapshot = cardBook.getProfileSnapshot();
        return new CardBookInfo(
                cardBook.getId(),
                cardBook.getCardId(),
                snapshot.name(),
                snapshot.title(),
                snapshot.company(),
                snapshot.profileImageUrl(),
                snapshot,
                cardBook.getGroupId(),
                cardBook.getMemo(),
                cardBook.isFavorite(),
                cardBook.getCreatedAt(),
                tagList
        );
    }
}
