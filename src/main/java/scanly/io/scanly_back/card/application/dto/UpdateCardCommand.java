package scanly.io.scanly_back.card.application.dto;

import java.util.List;

public record UpdateCardCommand(
        String memberId,
        String name,
        String title,
        String company,
        String phone,
        String email,
        String bio,
        List<SocialLinkCommand> socialLinks,
        String profileImageUrl,
        String portfolioUrl,
        String location
) {
}
