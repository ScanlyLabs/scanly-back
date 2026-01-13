package scanly.io.scanly_back.member.application.dto.command;

public record LoginCommand(
        String loginId,
        String password
) {
}
