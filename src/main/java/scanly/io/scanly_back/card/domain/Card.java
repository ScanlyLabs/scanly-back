package scanly.io.scanly_back.card.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Card extends BaseDomain {

    private String id;
    private final String memberId;
    private String name;
    private String title;
    private String company;
    private String phone;
    private String email;
    private String bio;
    private final List<SocialLink> socialLinks;
    private String profileImageUrl;
    private String portfolioUrl;
    private String location;
    private String qrImageUrl;

    private Card(String id, String memberId, String name, String title, String company,
                 String phone, String email, String bio, List<SocialLink> socialLinks,
                 String profileImageUrl, String portfolioUrl, String location, String qrImageUrl,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.title = title;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.socialLinks = socialLinks != null ? new ArrayList<>(socialLinks) : new ArrayList<>();
        this.profileImageUrl = profileImageUrl;
        this.portfolioUrl = portfolioUrl;
        this.location = location;
        this.qrImageUrl = qrImageUrl;
    }

    public static Card of(
            String id, String memberId, String name, String title,
            String company, String phone, String email, String bio, List<SocialLink> socialLinks,
            String profileImageUrl, String portfolioUrl, String location, String qrImageUrl,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Card(
                id, memberId, name, title, company, phone, email, bio, socialLinks,
                profileImageUrl, portfolioUrl, location, qrImageUrl,
                createdAt, updatedAt
        );
    }

    public static Card create(String memberId, String name, String title, String company,
                              String phone, String email, String bio, String profileImageUrl,
                              String portfolioUrl, String location) {
        return new Card(
                null,
                memberId,
                name,
                title,
                company,
                phone,
                email,
                bio,
                new ArrayList<>(),
                profileImageUrl,
                portfolioUrl,
                location,
                null,
                null,
                null
        );
    }

    /**
     * 명함 수정
     */
    public void update(String title, String company, String phone,
                       String email, String bio, String profileImageUrl,
                       String portfolioUrl, String location) {
        this.title = title;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.portfolioUrl = portfolioUrl;
        this.location = location;
    }

    /**
     * 소셜 링크 초기화
     */
    public void clearSocialLinks() {
        this.socialLinks.clear();
    }

    /**
     * 소셜 링크 추가
     * @param type 소셜 링크 유형
     * @param url 소셜 링크 url
     */
    public void addSocialLink(SocialLinkType type, String url) {
        if (this.socialLinks.size() >= 10) {
            throw new IllegalStateException("소셜 링크는 최대 10개까지 추가할 수 있습니다.");
        }
        int order = this.socialLinks.size();
        SocialLink socialLink = SocialLink.create(type, url, order);
        this.socialLinks.add(socialLink);
    }

    /**
     * QR 코드 이미지 설정
     * @param qrImageUrl QR 코드 이미지 (Base64)
     */
    public void assignQrCode(String qrImageUrl) {
        this.qrImageUrl = qrImageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getQrImageUrl() {
        return qrImageUrl;
    }

    public List<SocialLink> getSocialLinks() {
        return socialLinks;
    }
}
