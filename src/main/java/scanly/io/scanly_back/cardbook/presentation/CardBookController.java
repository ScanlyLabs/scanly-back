package scanly.io.scanly_back.cardbook.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookPreviewInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.RegisterCardBookInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.CardExchangeRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.SaveCardBookRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookFavoriteRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookMemoRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardBookPreviewResponse;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardBookResponse;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardExchangeResponse;
import scanly.io.scanly_back.cardbook.presentation.dto.response.RegisterCardBookResponse;
import scanly.io.scanly_back.common.ratelimit.RateLimiter;
import scanly.io.scanly_back.common.response.ApiResponse;
import scanly.io.scanly_back.common.response.PageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cardbooks/v1")
@Tag(name = "CardBook", description = "명함첩 API")
public class CardBookController {

    private final CardBookService cardBookService;

    @PostMapping
    @Operation(summary = "명함 저장", description = "타인의 명함을 내 명함첩에 저장합니다.")
    public ResponseEntity<ApiResponse<RegisterCardBookResponse>> saveCardBook(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody SaveCardBookRequest request
    ) {
        RegisterCardBookInfo registerCardBookInfo = cardBookService.save(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(RegisterCardBookResponse.from(registerCardBookInfo)));
    }

    @PostMapping("/exchange")
    @Operation(summary = "명함 교환", description = "타인에게 내 명함을 전송합니다.")
    @RateLimiter(key = "cardExchange")
    public ResponseEntity<ApiResponse<CardExchangeResponse>> cardExchange(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody CardExchangeRequest request
    ) {
        CardExchangeInfo cardExchangeInfo = cardBookService.cardExchange(request.toCommand(memberId));

        return ResponseEntity.ok(ApiResponse.success(CardExchangeResponse.from(cardExchangeInfo)));
    }

    @GetMapping
    @Operation(summary = "명함첩 목록 조회", description = "명함첩 목록을 페이징하여 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<CardBookPreviewResponse>>> readCardBookList(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "그룹 ID (선택)")
            @RequestParam(required = false) String groupId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<CardBookPreviewInfo> cardBookPreviewInfos = cardBookService.readAll(memberId, groupId, pageable);
        Page<CardBookPreviewResponse> cardBookPreviewResponses = cardBookPreviewInfos.map(CardBookPreviewResponse::from);

        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(cardBookPreviewResponses)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "명함첩 상세 조회", description = "특정 명함첩을 조회합니다.")
    public ResponseEntity<ApiResponse<CardBookResponse>> readCardBook(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함첩 ID", required = true)
            @PathVariable String id
    ) {
        CardBookInfo cardBookInfo = cardBookService.read(memberId, id);

        return ResponseEntity.ok(ApiResponse.success(CardBookResponse.from(cardBookInfo)));
    }

    @PostMapping("/{id}/group")
    @Operation(summary = "명함첩 그룹 수정", description = "명함첩의 소속 그룹을 수정합니다.")
    public ResponseEntity<ApiResponse<CardBookResponse>> updateGroup(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함첩 ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateCardBookGroupRequest request
    ) {
        CardBookInfo cardBookInfo = cardBookService.updateGroup(request.toCommand(memberId, id));

        return ResponseEntity.ok(ApiResponse.success(CardBookResponse.from(cardBookInfo)));
    }

    @PostMapping("/{id}/memo")
    @Operation(summary = "명함첩 메모 수정", description = "명함첩의 메모를 수정합니다.")
    public ResponseEntity<ApiResponse<CardBookResponse>> updateMemo(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함첩 ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateCardBookMemoRequest request
    ) {
        CardBookInfo cardBookInfo = cardBookService.updateMemo(request.toCommand(memberId, id));

        return ResponseEntity.ok(ApiResponse.success(CardBookResponse.from(cardBookInfo)));
    }

    @PostMapping("/{id}/favorite")
    @Operation(summary = "명함첩 즐겨찾기 수정", description = "명함첩의 즐겨찾기를 수정합니다.")
    public ResponseEntity<ApiResponse<CardBookResponse>> updateFavorite(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함첩 ID", required = true)
            @PathVariable String id,
            @RequestBody UpdateCardBookFavoriteRequest request
    ) {
        CardBookInfo cardBookInfo = cardBookService.updateFavorite(request.toCommand(memberId, id));

        return ResponseEntity.ok(ApiResponse.success(CardBookResponse.from(cardBookInfo)));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "명함첩 삭제", description = "특정 명함첩을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함첩 ID", required = true)
            @PathVariable String id
    ) {
        cardBookService.delete(memberId, id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/exists")
    @Operation(summary = "명함첩 존재 여부 확인", description = "명함첩 존재 여부를 확인합니다.")
    public ResponseEntity<ApiResponse<Boolean>> exists(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "명함 ID")
            @RequestParam String cardId
    ) {
        boolean exists = cardBookService.exists(memberId, cardId);
        return ResponseEntity.ok( ApiResponse.success(exists));
    }

}
