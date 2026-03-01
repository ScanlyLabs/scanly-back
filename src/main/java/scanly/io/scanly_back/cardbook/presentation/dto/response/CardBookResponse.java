package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;
import java.util.List;

public record CardBookResponse(
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
        List<Tag> tag
) {
    public static CardBookResponse from(CardBookInfo cardBookInfo) {
        return new CardBookResponse(
                cardBookInfo.id(),
                cardBookInfo.cardId(),
                cardBookInfo.name(),
                cardBookInfo.title(),
                cardBookInfo.company(),
                cardBookInfo.profileImageUrl(),
                cardBookInfo.profileSnapshot(),
                cardBookInfo.groupId(),
                cardBookInfo.memo(),
                cardBookInfo.isFavorite(),
                cardBookInfo.createdAt(),
                cardBookInfo.tagList()
        );
    }
}
