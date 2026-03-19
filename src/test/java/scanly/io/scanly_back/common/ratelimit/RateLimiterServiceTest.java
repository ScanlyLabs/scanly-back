package scanly.io.scanly_back.common.ratelimit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import scanly.io.scanly_back.IntegrationTestSupport;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RateLimiter 테스트")
class RateLimiterServiceTest extends IntegrationTestSupport {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String DAILY_LIMIT_KEY_PREFIX = "daily_limit:";

    @AfterEach
    void after() {
        Set<String> keys = redisTemplate.keys(DAILY_LIMIT_KEY_PREFIX+"*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    @DisplayName("[Happy] 일일 교환 제한 내에서는 허용된다")
    void isDailyExchangeAllowed() {
        // given
        String senderId = "sender-test";
        String receiverId = "receiver-test";
        int limit = 3;

        // when & then
        assertThat(rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, limit)).isTrue();
        assertThat(rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, limit)).isTrue();
        assertThat(rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, limit)).isTrue();
        // 4회부터 오류
        assertThat(rateLimiterService.isDailyExchangeAllowed(senderId, receiverId, limit)).isFalse();
    }
}