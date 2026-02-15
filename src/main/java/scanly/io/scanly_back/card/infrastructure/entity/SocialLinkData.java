package scanly.io.scanly_back.card.infrastructure.entity;

import scanly.io.scanly_back.card.domain.SocialLinkType;

/**
 * JSONB로 저장되는 소셜 링크 데이터
 */
public record SocialLinkData(
        SocialLinkType type,
        String url
) {
    public static SocialLinkData of(SocialLinkType type, String url) {
        return new SocialLinkData(type, url);
    }
}
