package scanly.io.scanly_back.member.persentaion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.member.application.dto.command.ChangePasswordCommand;

public record ChangePasswordRequest(
        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        String currentPassword,

        @NotBlank(message = "새 비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
        String newPassword
) {

    public ChangePasswordCommand toCommand() {
        return new ChangePasswordCommand(currentPassword, newPassword);
    }
}
