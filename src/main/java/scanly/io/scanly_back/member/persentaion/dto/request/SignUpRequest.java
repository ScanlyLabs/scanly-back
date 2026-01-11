package scanly.io.scanly_back.member.persentaion.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import scanly.io.scanly_back.member.application.dto.SignUpCommand;

public record SignUpRequest(
        @NotBlank(message = "로그인 아이디는 필수입니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 50, message = "이메일은 50자를 초과할 수 없습니다.")
        String email
) {

    /**
     * request dto -> command dto로 객체 변환
     * @return command dto
     */
    public SignUpCommand toCommand() {
        return new SignUpCommand(
                loginId,
                password,
                email
        );
    }
}
