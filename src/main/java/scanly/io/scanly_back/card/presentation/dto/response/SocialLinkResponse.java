package scanly.io.scanly_back.card.presentation.dto.response;

import scanly.io.scanly_back.card.application.dto.SocialLinkInfo;
import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkResponse(
        String id,
        SocialLinkType type,
        String url,
        int displayOrder
) {
    public static SocialLinkResponse from(SocialLinkInfo info) {
        return new SocialLinkResponse(
                info.id(),
                info.type(),
                info.url(),
                info.displayOrder()
        );
    }
}