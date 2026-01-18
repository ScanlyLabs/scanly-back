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
import scanly.io.scanly_back.cardbook.application.GroupService;
import scanly.io.scanly_back.cardbook.application.dto.info.GroupInfo;
import scanly.io.scanly_back.cardbook.presentation.dto.request.CreateGroupRequest;
import scanly.io.scanly_back.cardbook.presentation.dto.response.CreateGroupResponse;
import scanly.io.scanly_back.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/v1")
@Tag(name = "Group", description = "명함첩 그룹 API")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @Operation(summary = "명함첩 그룹 생성", description = "새로운 명함첩 그룹을 생성합니다.")
    public ResponseEntity<ApiResponse<CreateGroupResponse>> createGroup(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody CreateGroupRequest request
    ) {
        GroupInfo groupInfo = groupService.create(request.toCommand(memberId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CreateGroupResponse.from(groupInfo)));
    }
}
