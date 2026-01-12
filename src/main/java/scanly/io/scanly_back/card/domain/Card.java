package scanly.io.scanly_back.card.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Card {

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
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Card(String id, String memberId, String name, String title, String company,
                 String phone, String email, String bio, List<SocialLink> socialLinks,
                 String profileImageUrl,  String portfolioUrl, String location, String qrImageUrl,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
