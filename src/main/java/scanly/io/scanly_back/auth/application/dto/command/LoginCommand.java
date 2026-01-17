package scanly.io.scanly_back.auth.application.dto.command;

public record LoginCommand(
        String loginId,
        String password
) {
}
