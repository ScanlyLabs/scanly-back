package scanly.io.scanly_back.cardbook.domain.model;

import scanly.io.scanly_back.card.domain.Card;

public record ProfileSnapshot(
        String name,
        String title,
        String company,
        String phone,
        String email,
        String bio,
        String profileImageUrl,
        String portfolioUrl,
        String location
) {
    public static ProfileSnapshot from(Card card) {
        return new ProfileSnapshot(
                card.getName(),
                card.getTitle(),
                card.getCompany(),
                card.getPhone(),
                card.getEmail(),
                card.getBio(),
                card.getProfileImageUrl(),
                card.getPortfolioUrl(),
                card.getLocation()
        );
    }
}
