package scanly.io.scanly_back.card.application.dto.info;

import scanly.io.scanly_back.card.domain.Card;

import java.util.List;

public record ReadCardInfo(
        String id,
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
        String qrImageUrl
) {

    public static ReadCardInfo from(Card card) {
        return new ReadCardInfo(
                card.getId(),
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
                card.getQrImageUrl()
        );
    }
}
