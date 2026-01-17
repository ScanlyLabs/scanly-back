package scanly.io.scanly_back.auth.application.dto.command;

public record ReissueCommand(
        String refreshToken
) {
}
