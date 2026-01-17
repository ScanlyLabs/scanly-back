package scanly.io.scanly_back.member.persentaion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scanly.io.scanly_back.common.response.ApiResponse;
import scanly.io.scanly_back.member.application.MemberService;
import scanly.io.scanly_back.member.application.dto.info.SignUpInfo;
import scanly.io.scanly_back.member.persentaion.dto.request.SignUpRequest;
import scanly.io.scanly_back.member.persentaion.dto.response.SignUpResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/v1")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원 가입", description = "회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpInfo signUpInfo = memberService.signUp(request.toCommand());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SignUpResponse.from(signUpInfo)));
    }
}
