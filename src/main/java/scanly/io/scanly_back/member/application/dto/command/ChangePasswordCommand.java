package scanly.io.scanly_back.member.application.dto.command;

public record ChangePasswordCommand(
        String currentPassword,
        String newPassword
) {
}
