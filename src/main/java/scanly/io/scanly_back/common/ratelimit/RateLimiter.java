package scanly.io.scanly_back.common.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Rate Limiting 적용을 위한 어노테이션
 * 메서드에 적용하여 요청 횟수를 제한
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * Rate Limit 키 (필수)
     */
    String key();

    /**
     * 허용되는 최대 요청 횟수
     */
    int limit() default 10;

    /**
     * 시간 윈도우 크기
     */
    int window() default 1;

    /**
     * 시간 단위 (기본값: 분)
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
