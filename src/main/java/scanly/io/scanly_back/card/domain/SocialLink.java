package scanly.io.scanly_back.card.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;

public class SocialLink extends BaseDomain {

    private String id;
    private SocialLinkType type;
    private String url;
    private int displayOrder;

    private SocialLink(String id, SocialLinkType type, String url, int displayOrder,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.type = type;
        this.url = url;
        this.displayOrder = displayOrder;
    }

    public static SocialLink of(String id, SocialLinkType type, String url,
                                int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new SocialLink(id, type, url, displayOrder, createdAt, updatedAt);
    }

    public static SocialLink create(SocialLinkType type, String url, int displayOrder) {
        validateUrl(url);
        return new SocialLink(null, type, url, displayOrder, null, null);
    }

    /**
     * 소셜 링크 url 유효성 체크
     * @param url 소셜 링크 url
     */
    private static void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL은 필수입니다.");
        }
        if (url.length() > 500) {
            throw new IllegalArgumentException("URL은 500자를 초과할 수 없습니다.");
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public SocialLinkType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
