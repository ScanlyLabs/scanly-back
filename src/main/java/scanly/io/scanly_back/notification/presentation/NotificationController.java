package scanly.io.scanly_back.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scanly.io.scanly_back.common.response.ApiResponse;
import scanly.io.scanly_back.notification.application.NotificationService;
import scanly.io.scanly_back.notification.application.dto.info.NotificationInfo;
import scanly.io.scanly_back.notification.presentation.dto.response.NotificationResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/v1")
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 목록 조회", description = "알림 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAll(
            @AuthenticationPrincipal String memberId
    ) {
        List<NotificationInfo> notificationInfos = notificationService.getAll(memberId);
        List<NotificationResponse> notificationResponses
                = notificationInfos.stream().map(NotificationResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.success(notificationResponses));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "안 읽은 알림 수 조회", description = "안 읽은 알림 수를 조회합니다.")
    public ResponseEntity<ApiResponse<Integer>> getUnreadCount(
            @AuthenticationPrincipal String memberId
    ) {
        int count = notificationService.getUnreadCount(memberId);

        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
