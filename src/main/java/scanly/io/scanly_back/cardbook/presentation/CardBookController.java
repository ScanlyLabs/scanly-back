package scanly.io.scanly_back.cardbook.presentation;

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
import scanly.io.scanly_back.cardbook.application.CardBookService;
import scanly.io.scanly_back.cardbook.application.dto.info.CardBookInfo;
import scanly.io.scanly_back.cardbook.application.dto.info.CardExchangeInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.CardExchangeRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.SaveCardBookRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardBookResponse;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CardExchangeResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

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

    @PostMapping("/exchange")
    @Operation(summary = "명함 교환", description = "타인에게 내 명함을 전송합니다.")
    public ResponseEntity<ApiResponse<CardExchangeResponse>> cardExchange(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody CardExchangeRequest request
    ) {
        CardExchangeInfo cardExchangeInfo = cardBookService.cardExchange(request.toCommand(memberId));

        return ResponseEntity.ok(ApiResponse.success(CardExchangeResponse.from(cardExchangeInfo)));
    }
}
