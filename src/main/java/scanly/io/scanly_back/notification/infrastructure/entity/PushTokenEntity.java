package scanly.io.scanly_back.notification.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.common.entity.BaseEntity;
import scanly.io.scanly_back.notification.domain.model.PushPlatform;

@Getter
@Entity
@Table(name = "push_token")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;        // 회원 ID

    @Column(name = "token", nullable = false)
    private String token;           // expo push token

    @Enumerated(EnumType.STRING)
    @Column(name = "platfrom", nullable = false)
    private PushPlatform platform;        // 플랫폼

    public static PushTokenEntity of(
            String id, String memberId,
            String token, PushPlatform platform
    ) {
        return new PushTokenEntity(id, memberId, token, platform);
    }
}
