package scanly.io.scanly_back.notification.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import scanly.io.scanly_back.notification.application.dto.command.RegisterPushTokenCommand;
import scanly.io.scanly_back.notification.domain.model.PushPlatform;

public record RegisterPushTokenRequest(
        @NotBlank(message = "푸시 토큰값은 필수입니다.")
        String token,

        @NotNull(message = "플랫폼 정보는 필수입니다.")
        PushPlatform platform
) {
        public RegisterPushTokenCommand toCommand(String memberId) {
                return new RegisterPushTokenCommand(
                        token,
                        platform,
                        memberId
                );
        }
}
