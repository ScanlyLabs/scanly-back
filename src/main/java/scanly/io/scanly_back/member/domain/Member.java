package scanly.io.scanly_back.member.domain;

import java.time.LocalDateTime;

public class Member {

    private String id;
    private String loginId;                 // 로그인 아이디
    private String password;                // 비밀번호
    private String email;                   // 이메일
    private MemberStatus status;            // 회원 상태
    private LocalDateTime withdrawnAt;      // 탈퇴 요청일시
    private LocalDateTime createdAt;        // 생성 일시
    private LocalDateTime updatedAt;        // 수정 일시

    private Member(
            String id, String loginId, String password,
            String email, MemberStatus status,
            LocalDateTime withdrawnAt, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.status = status;
        this.withdrawnAt = withdrawnAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member signUP(String loginId, String password, String email) {
        LocalDateTime now = LocalDateTime.now();

        return new Member(
                null,
                loginId,
                password,
                email,
                MemberStatus.ACTIVE,
                null,
                now,
                now
        );
    }

    public static Member of(String id, String loginId, String password, String email, MemberStatus status, LocalDateTime withdrawnAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Member(
                id,
                loginId,
                password,
                email,
                status,
                withdrawnAt,
                createdAt,
                updatedAt
        );
    }

    // -- getters --
    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
