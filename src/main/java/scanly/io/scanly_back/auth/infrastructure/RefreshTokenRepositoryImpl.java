package scanly.io.scanly_back.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.auth.domain.RefreshToken;
import scanly.io.scanly_back.auth.domain.RefreshTokenRepository;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis를 활용한 Refresh Token 저장소 구현체
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private static final String KEY_PREFIX = "RT:";

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Refresh Token 저장
     * @param refreshToken 저장할 토큰 정보
     */
    @Override
    public void save(RefreshToken refreshToken) {
        String key = KEY_PREFIX + refreshToken.getMemberId();
        redisTemplate.opsForValue().set(
                key,
                refreshToken.getToken(),
                Duration.ofMillis(refreshToken.getExpiration())
        );
    }
}
