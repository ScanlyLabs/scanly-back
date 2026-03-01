package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.time.LocalDateTime;

public record RegisterCardBookResponse(
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
    public static RegisterCardBookResponse from(RegisterCardBookInfo cardBookInfo) {
        return new RegisterCardBookResponse(
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
                cardBookInfo.createdAt()
        );
    }
}
