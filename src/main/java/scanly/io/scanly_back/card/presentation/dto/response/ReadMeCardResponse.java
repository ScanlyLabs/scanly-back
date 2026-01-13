package scanly.io.scanly_back.card.presentation.dto.response;

import scanly.io.scanly_back.card.application.dto.info.ReadMeCardInfo;

import java.util.List;

public record ReadMeCardResponse(
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
    public static ReadMeCardResponse from(ReadMeCardInfo info) {
        return new ReadMeCardResponse(
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
