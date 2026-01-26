package scanly.io.scanly_back.notification.domain;

import scanly.io.scanly_back.common.domain.BaseDomain;
import scanly.io.scanly_back.notification.domain.model.PushPlatform;

import java.time.LocalDateTime;

public class PushToken extends BaseDomain {
    private String id;
    private String memberId;        // 회원 ID
    private String token;           // expo push token
    private PushPlatform platform;        // 플랫폼

    private PushToken(
            String id, String memberId,
            String token, PushPlatform platform,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.memberId = memberId;
        this.token = token;
        this.platform = platform;
    }

    public static PushToken of(
            String id, String memberId,
            String token, PushPlatform platform,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new PushToken(id, memberId, token, platform, createdAt, updatedAt);
    }

    public static PushToken create(String memberId, String token, PushPlatform platform) {
        return new PushToken(
                null,
                memberId,
                token,
                platform,
                null,
                null
        );
    }

    // getters

    public String getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getToken() {
        return token;
    }

    public PushPlatform getPlatform() {
        return platform;
    }

    public void update(String token, PushPlatform platform) {
        this.token = token;
        this.platform = platform;
    }
}
