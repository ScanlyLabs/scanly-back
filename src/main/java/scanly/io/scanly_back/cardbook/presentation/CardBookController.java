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
import scanly.io.scanly_back.cardbook.presentation.dto.request.SaveCardBookRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookFavoriteRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateCardBookMemoRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardBookResponse;
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
    public ResponseEntity<ApiResponse<CardBookResponse>> saveCardBook(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody SaveCardBookRequest request
    ) {
        CardBookInfo cardBookInfo = cardBookService.save(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CardBookResponse.from(cardBookInfo)));
    }

    @GetMapping
    @Operation(summary = "명함첩 목록 조회", description = "명함첩 목록을 페이징하여 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<CardBookResponse>>> readCardBookList(
            @AuthenticationPrincipal String memberId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<CardBookInfo> cardBookInfos = cardBookService.readAll(memberId, pageable);
        Page<CardBookResponse> cardBookResponses = cardBookInfos.map(CardBookResponse::from);

        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(cardBookResponses)));
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
}
