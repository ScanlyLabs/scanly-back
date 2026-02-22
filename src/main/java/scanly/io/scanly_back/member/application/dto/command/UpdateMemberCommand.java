package scanly.io.scanly_back.member.application.dto.command;

public record UpdateMemberCommand(
        String name,
        String email
) {
}
