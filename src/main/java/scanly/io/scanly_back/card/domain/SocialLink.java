package scanly.io.scanly_back.card.domain;

public class SocialLink {

    private SocialLinkType type;
    private String url;

    private SocialLink(SocialLinkType type, String url) {
        this.type = type;
        this.url = url;
    }

    public static SocialLink of(SocialLinkType type, String url) {
        return new SocialLink(type, url);
    }

    public static SocialLink create(SocialLinkType type, String url) {
        validateUrl(url);
        return new SocialLink(type, url);
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
    public SocialLinkType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
