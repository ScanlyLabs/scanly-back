package scanly.io.scanly_back.card.application.dto.info;

import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkInfo(
        SocialLinkType type,
        String url
) {
    public static SocialLinkInfo from(SocialLink socialLink) {
        return new SocialLinkInfo(
                socialLink.getType(),
                socialLink.getUrl()
        );
    }
}