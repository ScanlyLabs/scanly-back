package scanly.io.scanly_back.member.persentaion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import scanly.io.scanly_back.common.response.ApiResponse;
import scanly.io.scanly_back.member.application.MemberService;
import scanly.io.scanly_back.member.application.dto.info.ReadMemberInfo;
import scanly.io.scanly_back.member.application.dto.info.SignUpInfo;
import scanly.io.scanly_back.member.application.dto.info.UpdateMemberInfo;
import scanly.io.scanly_back.member.persentaion.dto.request.ChangePasswordRequest;
import scanly.io.scanly_back.member.persentaion.dto.request.SignUpRequest;
import scanly.io.scanly_back.member.persentaion.dto.request.UpdateMemberRequest;
import scanly.io.scanly_back.member.persentaion.dto.response.ReadMemberResponse;
import scanly.io.scanly_back.member.persentaion.dto.response.SignUpResponse;
import scanly.io.scanly_back.member.persentaion.dto.response.UpdateMemberResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/v1")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원 가입", description = "회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpInfo signUpInfo = memberService.signUp(request.toCommand());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SignUpResponse.from(signUpInfo)));
    }

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<ReadMemberResponse>> readMe(
            @AuthenticationPrincipal String memberId
    ) {
        ReadMemberInfo readMemberInfo = memberService.readMe(memberId);

        return ResponseEntity
                .ok(ApiResponse.success(ReadMemberResponse.from(readMemberInfo)));
    }

    @PostMapping("/me/update")
    @Operation(summary = "내 정보 수정", description = "현재 로그인한 회원의 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<UpdateMemberResponse>> updateMe(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody UpdateMemberRequest request
    ) {
        UpdateMemberInfo updateMemberInfo = memberService.updateMe(memberId, request.toCommand());

        return ResponseEntity
                .ok(ApiResponse.success(UpdateMemberResponse.from(updateMemberInfo)));
    }

    @PostMapping("/me/password")
    @Operation(summary = "비밀번호 변경", description = "현재 로그인한 회원의 비밀번호를 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal String memberId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        memberService.changePassword(memberId, request.toCommand());

        return ResponseEntity
                .ok(ApiResponse.success(null));
    }
}
