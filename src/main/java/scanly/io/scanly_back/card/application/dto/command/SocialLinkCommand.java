package scanly.io.scanly_back.card.application.dto.command;

import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkCommand(
        SocialLinkType type,
        String url
) {}