package scanly.io.scanly_back.card.application.dto.info;

import scanly.io.scanly_back.card.domain.Card;

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
