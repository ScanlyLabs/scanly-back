package scanly.io.scanly_back.card.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scanly.io.scanly_back.card.application.CardService;
import scanly.io.scanly_back.card.application.dto.RegisterCardInfo;
import scanly.io.scanly_back.card.presentation.dto.RegisterCardResponse;
import scanly.io.scanly_back.card.presentation.dto.RegisterCardRequest;
import scanly.io.scanly_back.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards/v1")
@Tag(name = "Card", description = "명함 API")
public class CardController {

    private final CardService cardService;

    @PostMapping
    @Operation(summary = "명함 생성", description = "새로운 명함을 등록합니다.")
    public ResponseEntity<ApiResponse<RegisterCardResponse>> registerCard(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId,
            @Valid @RequestBody RegisterCardRequest request
    ) {
        RegisterCardInfo registerCardInfo = cardService.registerCard(request.toCommand(memberId));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(RegisterCardResponse.from(registerCardInfo)));
    }
}
