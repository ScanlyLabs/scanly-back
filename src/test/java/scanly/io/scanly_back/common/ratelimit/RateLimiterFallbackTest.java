package scanly.io.scanly_back.common.ratelimit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import scanly.io.scanly_back.IntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("RateLimiter Fallback 테스트")
// 실제 Redis 연결 방지
@EnableAutoConfiguration(exclude = {
        DataRedisAutoConfiguration.class,
        DataRedisRepositoriesAutoConfiguration.class
})
class RateLimiterFallbackTest extends IntegrationTestSupport {
    @Autowired
    private RateLimiterService rateLimiterService;

    @MockitoBean
    private RedisTemplate<String, String> redisTemplate;

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    @DisplayName("[Bad] 일일 제한 체크 중 Redis 실패 시 fallback 으로 true 반환")
    void isDailyExchangeAllowedFail() {
        // given
        String senderId = "sender-1";
        String receiverId = "receiver-1";
        int limit = 3;
        given(redisTemplate.opsForValue())
                .willThrow(new RuntimeException("Redis connection failed"));

        // when
        boolean result = rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, limit);

        // then
        assertThat(result).isTrue();
    }
}
