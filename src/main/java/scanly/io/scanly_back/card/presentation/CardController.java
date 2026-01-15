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
import scanly.io.scanly_back.card.application.dto.info.ReadCardInfo;
import scanly.io.scanly_back.card.application.dto.info.RegisterCardInfo;
import scanly.io.scanly_back.card.presentation.dto.response.ReadCardResponse;
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
    public ResponseEntity<ApiResponse<ReadCardResponse>> readMyCard(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ReadCardInfo cardInfo = cardService.readMyCard(memberId);

        return ResponseEntity.ok(ApiResponse.success(ReadCardResponse.from(cardInfo)));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "회원 명함 조회", description = "특정 회원의 명함을 조회합니다.")
    public ResponseEntity<ApiResponse<ReadCardResponse>> readCard(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable String memberId
    ) {
        ReadCardInfo cardInfo = cardService.readMyCard(memberId);

        return ResponseEntity.ok(ApiResponse.success(ReadCardResponse.from(cardInfo)));
    }

    @PostMapping("/me/update")
    @Operation(summary = "내 명함 수정", description = "내 명함을 수정합니다.")
    public ResponseEntity<ApiResponse<ReadCardResponse>> updateCard(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId,
            @Valid @RequestBody UpdateCardRequest request
    ) {
        ReadCardInfo cardInfo = cardService.updateCard(request.toCommand(memberId));

        return ResponseEntity.ok(ApiResponse.success(ReadCardResponse.from(cardInfo)));
    }

    @PostMapping("/me/delete")
    @Operation(summary = "내 명함 삭제", description = "내 명함을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteMyCard(
            @Parameter(description = "회원 ID", required = true)
            @RequestHeader("X-Member-Id") String memberId
    ) {
        cardService.deleteMyCard(memberId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
