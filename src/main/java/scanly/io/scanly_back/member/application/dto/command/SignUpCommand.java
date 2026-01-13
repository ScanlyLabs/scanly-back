package scanly.io.scanly_back.member.application.dto.command;

public record SignUpCommand(
        String loginId,
        String password,
        String email
) {
}
