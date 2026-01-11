package scanly.io.scanly_back.member.application.dto;

public record LoginCommand(
        String loginId,
        String password
) {
}
