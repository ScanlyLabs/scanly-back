package scanly.io.scanly_back.cardbook.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.SaveCardBookRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardBookResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

import java.util.List;

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
    @Operation(summary = "명함첩 목록 조회", description = "명함첩 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CardBookResponse>>> getCardBookList(
            @AuthenticationPrincipal String memberId
    ) {
        List<CardBookInfo> cardBookInfos = cardBookService.getAll(memberId);
        List<CardBookResponse> cardBookResponses
                = cardBookInfos.stream().map(CardBookResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.success(cardBookResponses));
    }
}
