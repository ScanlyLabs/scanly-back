package scanly.io.scanly_back.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scanly.io.scanly_back.auth.application.AuthService;
import scanly.io.scanly_back.auth.application.dto.info.LoginInfo;
import scanly.io.scanly_back.auth.presentation.dto.request.LoginRequest;
import scanly.io.scanly_back.auth.presentation.dto.response.LoginResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인하고 JWT 토큰을 발급받습니다.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginInfo loginInfo = authService.login(request.toCommand());
        return ResponseEntity.ok(ApiResponse.success(LoginResponse.from(loginInfo)));
    }

}
