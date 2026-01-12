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
import scanly.io.scanly_back.card.application.dto.ReadMeCardInfo;
import scanly.io.scanly_back.card.application.dto.RegisterCardInfo;
import scanly.io.scanly_back.card.presentation.dto.response.ReadMeCardResponse;
import scanly.io.scanly_back.card.presentation.dto.response.RegisterCardResponse;
import scanly.io.scanly_back.card.presentation.dto.request.RegisterCardRequest;
import scanly.io.scanly_back.card.presentation.dto.request.UpdateCardRequest;
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

    @GetMapping("/me")
    @Operation(summary = "내 명함 조회", description = "내 명함을 조회합니다.")
    public ResponseEntity<ApiResponse<ReadMeCardResponse>> readMe(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ReadMeCardInfo cardInfo = cardService.readMe(memberId);

        return ResponseEntity.ok(ApiResponse.success(ReadMeCardResponse.from(cardInfo)));
    }

    @PostMapping("/me/update")
    @Operation(summary = "내 명함 수정", description = "내 명함을 수정합니다.")
    public ResponseEntity<ApiResponse<ReadMeCardResponse>> updateCard(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId,
            @Valid @RequestBody UpdateCardRequest request
    ) {
        ReadMeCardInfo cardInfo = cardService.updateCard(request.toCommand(memberId));

        return ResponseEntity.ok(ApiResponse.success(ReadMeCardResponse.from(cardInfo)));
    }
}
