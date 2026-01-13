package scanly.io.scanly_back.card.application.dto.info;

import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkInfo(
        String id,
        SocialLinkType type,
        String url,
        int displayOrder
) {
    public static SocialLinkInfo from(SocialLink socialLink) {
        return new SocialLinkInfo(
                socialLink.getId(),
                socialLink.getType(),
                socialLink.getUrl(),
                socialLink.getDisplayOrder()
        );
    }
}