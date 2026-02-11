package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.CardBookPreviewInfo;

import java.time.LocalDateTime;

public record CardBookPreviewResponse(
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
    public static CardBookPreviewResponse from(CardBookPreviewInfo cardBookPreviewInfo) {
        return new CardBookPreviewResponse(
                cardBookPreviewInfo.id(),
                cardBookPreviewInfo.cardId(),
                cardBookPreviewInfo.name(),
                cardBookPreviewInfo.title(),
                cardBookPreviewInfo.company(),
                cardBookPreviewInfo.profileImageUrl(),
                cardBookPreviewInfo.groupId(),
                cardBookPreviewInfo.memo(),
                cardBookPreviewInfo.isFavorite(),
                cardBookPreviewInfo.createdAt()
        );
    }
}
