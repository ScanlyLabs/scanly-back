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
import scanly.io.scanly_back.cardbook.application.dto.TagService;
import scanly.io.scanly_back.cardbook.application.dto.info.TagInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.RegisterTagRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.RegisterTagResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags/v1")
@Tag(name = "Tag", description = "태그 API")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(summary = "태그 저장", description = "명함첩 내 태그를 저장합니다.")
    public ResponseEntity<ApiResponse<RegisterTagResponse>> registerTag(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody RegisterTagRequest request
    ) {
        TagInfo tagInfo = tagService.register(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(RegisterTagResponse.from(tagInfo)));
    }
}
