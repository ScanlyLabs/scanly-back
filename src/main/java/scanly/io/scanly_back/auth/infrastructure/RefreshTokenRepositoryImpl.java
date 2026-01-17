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

    /**
     * 회원 ID로 Refresh Token 조회
     * @param memberId 회원 ID
     * @return Refresh Token (없으면 empty)
     */
    @Override
    public Optional<String> findByMemberId(String memberId) {
        String key = KEY_PREFIX + memberId;
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    /**
     * 회원 아이디로 Refresh Token 삭제
     * @param memberId 회원 아이디
     */
    @Override
    public void deleteByMemberId(String memberId) {
        String key = KEY_PREFIX + memberId;
        redisTemplate.delete(key);
    }
}
