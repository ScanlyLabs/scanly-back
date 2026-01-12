package scanly.io.scanly_back.card.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.card.application.dto.SocialLinkCommand;
import scanly.io.scanly_back.card.domain.SocialLinkType;

public record SocialLinkRequest(
        SocialLinkType type,
        @NotBlank(message = "URL은 필수입니다.")
        @Size(max = 500, message = "URL은 500자를 초과할 수 없습니다.")
        String url
) {
    public SocialLinkCommand toCommand() {
        return new SocialLinkCommand(type, url);
    }
}
