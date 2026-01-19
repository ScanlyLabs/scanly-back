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
import scanly.io.scanly_back.cardbook.application.GroupService;
import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.CreateGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.RenameGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.request.ReorderGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.GroupResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/v1")
@Tag(name = "Group", description = "명함첩 그룹 API")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "명함첩 그룹 생성", description = "새로운 명함첩 그룹을 생성합니다.")
    public ResponseEntity<ApiResponse<GroupResponse>> createGroup(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody CreateGroupRequest request
    ) {
        GroupInfo groupInfo = groupService.create(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(GroupResponse.from(groupInfo)));
    }

    @PostMapping("/{id}/rename")
    @Operation(summary = "명함첩 그룹명 수정", description = "특정 명함첩 그룹명을 수정합니다.")
    public ResponseEntity<ApiResponse<GroupResponse>> rename(
            @Parameter(description = "명함첩 그룹 ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody RenameGroupRequest request
    ) {
        GroupInfo groupInfo = groupService.rename(request.toCommand(id));

        return ResponseEntity.ok(ApiResponse.success(GroupResponse.from(groupInfo)));
    }

    @PostMapping("/reorder")
    @Operation(summary = "명함첩 그룹 순서 변경", description = "여러 명함첩 그룹의 순서를 한 번에 변경합니다.")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> reorder(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody ReorderGroupRequest request
    ) {
        List<GroupInfo> groupInfos = groupService.reorder(request.toCommand(memberId));

        List<GroupResponse> responses = groupInfos.stream()
                .map(GroupResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "명함첩 그룹 삭제", description = "특정 명함첩 그룹을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @Parameter(description = "명함첩 그룹 ID", required = true)
            @PathVariable String id
    ) {
        groupService.deleteGroup(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
