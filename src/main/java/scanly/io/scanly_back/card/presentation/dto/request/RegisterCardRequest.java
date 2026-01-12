package scanly.io.scanly_back.card.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.card.application.dto.RegisterCardCommand;
import scanly.io.scanly_back.card.application.dto.SocialLinkCommand;
import scanly.io.scanly_back.card.domain.SocialLinkType;

import java.util.List;

public record RegisterCardRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 30, message = "이름은 2-30자여야 합니다.")
        String name,

        @NotBlank(message = "직함은 필수입니다.")
        @Size(min = 2, max = 50, message = "직함은 2-50자여야 합니다.")
        String title,

        @NotBlank(message = "회사명은 필수입니다.")
        @Size(min = 2, max = 50, message = "회사명은 2-50자여야 합니다.")
        String company,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^\\d{11}$", message = "전화번호는 숫자 11자리여야 합니다.")
        String phone,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
        String email,

        @Size(max = 300, message = "자기소개는 300자를 초과할 수 없습니다.")
        String bio,

        @Valid
        @Size(max = 10, message = "소셜 링크는 최대 10개까지 추가할 수 있습니다.")
        List<SocialLinkRequest> socialLinks,

        String profileImageUrl,

        String portfolioUrl,

        @Size(max = 100, message = "위치는 100자를 초과할 수 없습니다.")
        String location
) {
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

    public RegisterCardCommand toCommand(String memberId) {
        List<SocialLinkCommand> linkCommands = null;
        if (socialLinks != null) {
            linkCommands = socialLinks.stream()
                    .map(SocialLinkRequest::toCommand)
                    .toList();
        }

        return new RegisterCardCommand(
                memberId,
                name,
                title,
                company,
                phone,
                email,
                bio,
                linkCommands,
                profileImageUrl,
                portfolioUrl,
                location
        );
    }
}
