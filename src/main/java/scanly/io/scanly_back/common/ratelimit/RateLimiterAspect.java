package scanly.io.scanly_back.common.ratelimit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import scanly.io.scanly_back.common.exception.CustomException;
import scanly.io.scanly_back.common.exception.ErrorCode;

/**
 * Rate Limiter AOP Aspect
 * @RateLimiter 어노테이션이 적용된 메서드에 Rate Limiting을 적용
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RateLimiterService rateLimiterService;

    @Around("@annotation(rateLimiter)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        // Rate Limit 키 생성
        String key = resolveKey(rateLimiter);

        // Rate Limit 호출
        boolean allowed = rateLimiterService.isAllowed(
                key,
                rateLimiter.limit(),
                rateLimiter.window(),
                rateLimiter.timeUnit()
        );

        // 제한 횟수 초과
        if (!allowed) {
            long timeToReset = rateLimiterService.getTimeToReset(key);
            log.warn("Rate limit exceeded for key: {}, reset in {} seconds", key, timeToReset);
            throw new CustomException(ErrorCode.RATE_LIMIT_EXCEEDED);
        }

        return joinPoint.proceed();
    }

    /**
     * Rate Limit 키 생성
     */
    private String resolveKey(RateLimiter rateLimiter) {
        String userId = getCurrentUserId();
        return userId + ":" + rateLimiter.key();
    }

    /**
     * 현재 인증된 사용자 ID를 가져오기
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                return (String) principal;
            }
        }
        return "anonymous";
    }
}
