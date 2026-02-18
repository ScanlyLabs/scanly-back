package scanly.io.scanly_back.common.ratelimit;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * Redis 기반 Rate Limiter 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final String DAILY_LIMIT_KEY_PREFIX = "daily_limit:";
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 요청이 허용되는지 확인하고, 허용되면 카운트 증가
     * @param key      Rate Limit 키
     * @param limit    허용되는 최대 요청 횟수
     * @param window   시간 윈도우 크기
     * @param timeUnit 시간 단위
     * @return 요청이 허용되면 true, 아니면 false
     */
    @CircuitBreaker(name = "redisCircuitBreaker", fallbackMethod = "isAllowedFallback")
    public boolean isAllowed(String key, int limit, int window, TimeUnit timeUnit) {
        String redisKey = RATE_LIMIT_KEY_PREFIX + key;
        Duration windowDuration = Duration.of(window, timeUnit.toChronoUnit());

        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == null) {
            return false;
        }

        // 첫 번째 요청인 경우 TTL 설정
        if (currentCount == 1) {
            redisTemplate.expire(redisKey, windowDuration);
        }

        return currentCount <= limit;
    }

    private boolean isAllowedFallback(String key, int limit, int window, TimeUnit timeUnit, Exception e) {
        log.warn("Redis Rate Limit fallback(사용자별 명함 교환 횟수 조회 실패), key: {}, error: {}", key, e.getMessage());
        return true;
    }

    /**
     * TTL을 초 단위로 반환
     * @param key Rate Limit 키
     * @return 남은 시간(초), 키가 없으면 -1
     */
    @CircuitBreaker(name = "redisCircuitBreaker", fallbackMethod = "getTimeToResetFallback")
    public long getTimeToReset(String key) {
        String redisKey = RATE_LIMIT_KEY_PREFIX + key;
        Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
        return ttl != null ? ttl : -1;
    }

    private long getTimeToResetFallback(String key, Exception e) {
        log.warn("Redis Rate Limit time reset fallback(TTL 조회 실패), key: {}, error: {}", key, e.getMessage());
        return -1;
    }

    /**
     * 일일 제한을 확인하고, 허용되면 카운트 증가 (다음날 자정 리셋)
     * @param senderId   발신자 ID
     * @param receiverId 수신자 ID
     * @param dailyLimit 일일 허용 횟수
     * @return 요청이 허용되면 true, 아니면 false
     */
    @CircuitBreaker(name = "redisCircuitBreaker", fallbackMethod = "isDailyExchangeAllowedFallback")
    public boolean isDailyExchangeAllowed(String senderId, String receiverId, int dailyLimit) {
        String today = LocalDate.now().toString();
        String redisKey = DAILY_LIMIT_KEY_PREFIX + "exchange:" + senderId + ":" + receiverId + ":" + today;

        // Redis가 응답하지 않거나 오류 날 경우
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == null) {
            return false;
        }

        // 첫 번째 요청인 경우 다음날 자정까지 TTL 설정
        if (currentCount == 1) {
            Duration ttl = Duration.between(
                    LocalDateTime.now(),
                    LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT)
            );
            redisTemplate.expire(redisKey, ttl);
        }

        return currentCount <= dailyLimit;
    }

    private boolean isDailyExchangeAllowedFallback(String senderId, String receiverId, int dailyLimit, Exception e) {
        log.warn("Redis Daily Rate Limit fallback(발신자-수신자 쌍 일일 교환 횟수 조회 실패), senderId: {}, receiverId: {}, error: {}", senderId, receiverId, e.getMessage());
        return true;
    }
}
