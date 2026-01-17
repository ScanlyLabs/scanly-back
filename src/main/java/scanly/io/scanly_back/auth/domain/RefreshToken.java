package scanly.io.scanly_back.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshToken {

    private final String memberId;
    private final String token;
    private final long expiration;

    public static RefreshToken create(String memberId, String token, long expiration) {
        return new RefreshToken(memberId, token, expiration);
    }
}
