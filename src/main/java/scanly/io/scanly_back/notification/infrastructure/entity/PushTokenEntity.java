package scanly.io.scanly_back.notification.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "push_token")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;        // 회원 ID

    @Column(name = "token", nullable = false)
    private String token;           // expo push token

    @Column(name = "platfrom", nullable = false)
    private String platform;        // 플랫폼

    public static PushTokenEntity of(
            String id, String memberId,
            String token, String platform
    ) {
        return new PushTokenEntity(id, memberId, token, platform);
    }
}
