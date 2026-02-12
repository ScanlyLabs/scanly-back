package scanly.io.scanly_back.cardbook.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import scanly.io.scanly_back.cardbook.application.dto.TagService;
import scanly.io.scanly_back.cardbook.application.dto.info.TagInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.RegisterTagRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.UpdateTagRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.TagResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags/v1")
@Tag(name = "Tag", description = "태그 API")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(summary = "태그 저장", description = "명함첩 내 태그를 저장합니다.")
    public ResponseEntity<ApiResponse<TagResponse>> register(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody RegisterTagRequest request
    ) {
        TagInfo tagInfo = tagService.register(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(TagResponse.from(tagInfo)));
    }

    @PostMapping("/{id}/update")
    @Operation(summary = "태그 수정", description = "명함첩 내 태그를 수합니다.")
    public ResponseEntity<ApiResponse<TagResponse>> update(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "태그 ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody UpdateTagRequest request
    ) {
        TagInfo tagInfo = tagService.update(request.toCommand(memberId, id));

        return ResponseEntity.ok(ApiResponse.success(TagResponse.from(tagInfo)));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "태그 삭제", description = "명함첩 내 태그를 삭합니다.")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "태그 ID", required = true)
            @PathVariable String id
    ) {
        tagService.delete(memberId, id);

        return ResponseEntity.ok(ApiResponse.success());
    }
}
