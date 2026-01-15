package scanly.io.scanly_back.card.presentation.dto.response;

import scanly.io.scanly_back.card.application.dto.info.ReadCardInfo;

import java.util.List;

public record ReadCardResponse(
        String id,
        String name,
        String title,
        String company,
        String phone,
        String email,
        String bio,
        List<SocialLinkResponse> socialLinks,
        String profileImageUrl,
        String portfolioUrl,
        String location,
        String qrImageUrl
) {
    public static ReadCardResponse from(ReadCardInfo info) {
        return new ReadCardResponse(
            info.id(),
                info.name(),
                info.title(),
                info.company(),
                info.phone(),
                info.email(),
                info.bio(),
                info.socialLinks().stream()
                        .map(SocialLinkResponse::from)
                        .toList(),
                info.profileImageUrl(),
                info.portfolioUrl(),
                info.location(),
                info.qrImageUrl()
        );
    }
}
