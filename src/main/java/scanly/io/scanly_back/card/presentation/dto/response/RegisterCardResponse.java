package scanly.io.scanly_back.card.presentation.dto.response;

import scanly.io.scanly_back.card.application.dto.RegisterCardInfo;
import scanly.io.scanly_back.card.domain.SocialLinkType;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterCardResponse(
        String id,
        String memberId,
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
        String qrImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static RegisterCardResponse from(RegisterCardInfo info) {
        return new RegisterCardResponse(
                info.id(),
                info.memberId(),
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
                info.qrImageUrl(),
                info.createdAt(),
                info.updatedAt()
        );
    }
}
