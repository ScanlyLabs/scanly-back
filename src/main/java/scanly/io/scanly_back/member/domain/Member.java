package scanly.io.scanly_back.member.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;

import java.time.LocalDateTime;

public class Member extends BaseDomain {

    private String id;
    private String loginId;
    private String name;
    private String password;
    private String email;
    private MemberStatus status;
    private LocalDateTime withdrawnAt;

    private Member(
            String id, String loginId, String name,
            String password, String email, MemberStatus status,
            LocalDateTime withdrawnAt, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
        this.withdrawnAt = withdrawnAt;
    }

    public static Member signUP(String loginId, String name, String password, String email) {
        return new Member(
                null,
                loginId,
                name,
                password,
                email,
                MemberStatus.ACTIVE,
                null,
                null,
                null
        );
    }

    public static Member of(
            String id, String loginId, String name,
            String password, String email,
            MemberStatus status, LocalDateTime withdrawnAt,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Member(
                id,
                loginId,
                name,
                password,
                email,
                status,
                withdrawnAt,
                createdAt,
                updatedAt
        );
    }

    /**
     * 회원 정보 수정
     * @param name 회원명
     * @param email 이메일
     */
    public void updateInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // -- getters --
    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getName() {
        return name;
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
}
