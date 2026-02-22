package scanly.io.scanly_back.member.persentaion.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.member.application.dto.command.UpdateMemberCommand;

public record UpdateMemberRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 20, message = "이름은 2~20자여야 합니다.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z]+$",
                message = "이름은 한글 또는 영문만 입력할 수 있습니다."
        )
        String name,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
        String email
) {

    public UpdateMemberCommand toCommand() {
        return new UpdateMemberCommand(name, email);
    }
}
