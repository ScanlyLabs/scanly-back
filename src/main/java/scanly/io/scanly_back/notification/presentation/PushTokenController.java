package scanly.io.scanly_back.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scanly.io.scanly_back.common.response.ApiResponse;
import scanly.io.scanly_back.notification.application.PushTokenService;
import scanly.io.scanly_back.notification.application.dto.info.PushTokenInfo;
import scanly.io.scanly_back.notification.presentation.dto.request.RegisterPushTokenRequest;
import scanly.io.scanly_back.notification.presentation.dto.response.PushTokenResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/push-tokens/v1")
@Tag(name = "PushToken", description = "푸시 토큰 API")
public class PushTokenController {

    private final PushTokenService pushTokenService;

    @PostMapping
    @Operation(summary = "푸시 토큰 등록", description = "푸시 토큰을 등록합니다.")
    public ResponseEntity<ApiResponse<PushTokenResponse>> register(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody RegisterPushTokenRequest request
    ) {
        PushTokenInfo pushTokenInfo = pushTokenService.register(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(PushTokenResponse.from(pushTokenInfo)));
    }
}
