package scanly.io.scanly_back.card.application.dto;

import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.domain.SocialLinkType;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterCardInfo(
        String id,
        String memberId,
        String name,
        String title,
        String company,
        String phone,
        String email,
        String bio,
        List<SocialLinkInfo> socialLinks,
        String profileImageUrl,
        String portfolioUrl,
        String location,
        String qrImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
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

    public static RegisterCardInfo from(Card card) {
        return new RegisterCardInfo(
                card.getId(),
                card.getMemberId(),
                card.getName(),
                card.getTitle(),
                card.getCompany(),
                card.getPhone(),
                card.getEmail(),
                card.getBio(),
                card.getSocialLinks().stream()
                        .map(SocialLinkInfo::from)
                        .toList(),
                card.getProfileImageUrl(),
                card.getPortfolioUrl(),
                card.getLocation(),
                card.getQrImageUrl(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}
