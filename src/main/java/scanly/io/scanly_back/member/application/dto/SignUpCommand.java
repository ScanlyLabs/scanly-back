package scanly.io.scanly_back.member.application.dto;

public record SignUpCommand(
        String loginId,
        String password,
        String email
) {
}
