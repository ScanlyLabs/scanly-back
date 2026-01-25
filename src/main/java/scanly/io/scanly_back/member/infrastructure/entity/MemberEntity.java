package scanly.io.scanly_back.member.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.common.entity.BaseEntity;
import scanly.io.scanly_back.member.domain.MemberStatus;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "login_id", nullable = false, unique = true, updatable = false)
    private String loginId;                         // 로그인 아이디

    @Column(name = "name", nullable = false, length = 20)
    private String name;                            // 회원명

    @Column(name = "password", nullable = false, unique = true)
    private String password;                        // 비밀번호

    @Column(name = "email", length = 50)
    private String email;                           // 이메일

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;                    // 회원 상태

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;              // 탈퇴 요청일시

    public static MemberEntity of(
            String id, String loginId, String name, String password,
            String email, MemberStatus status, LocalDateTime withdrawnAt
    ) {
        return new MemberEntity(id, loginId, name, password, email, status, withdrawnAt);
    }
}
