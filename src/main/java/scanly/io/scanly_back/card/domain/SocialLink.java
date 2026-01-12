package scanly.io.scanly_back.card.domain;

import java.time.LocalDateTime;

public class SocialLink {

    private String id;
    private SocialLinkType type;
    private String url;
    private int displayOrder;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private SocialLink(String id, SocialLinkType type, String url, int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SocialLink of(String id, SocialLinkType type, String url,
                                int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new SocialLink(id, type, url, displayOrder, createdAt, updatedAt);
    }

    public static SocialLink create(SocialLinkType type, String url, int displayOrder) {
        validateUrl(url);
        return new SocialLink(null, type, url, displayOrder, LocalDateTime.now(), LocalDateTime.now());
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
