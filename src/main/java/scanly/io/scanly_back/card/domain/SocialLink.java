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
