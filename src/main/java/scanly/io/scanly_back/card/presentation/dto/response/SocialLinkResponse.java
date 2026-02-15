package scanly.io.scanly_back.card.presentation.dto.response;

import scanly.io.scanly_back.card.application.dto.info.SocialLinkInfo;
import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkResponse(
        SocialLinkType type,
        String url
) {
    public static SocialLinkResponse from(SocialLinkInfo info) {
        return new SocialLinkResponse(
                info.type(),
                info.url()
        );
    }
}