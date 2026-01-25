package scanly.io.scanly_back.member.application.dto.command;

public record SignUpCommand(
        String loginId,
        String name,
        String password,
        String email
) {
}
