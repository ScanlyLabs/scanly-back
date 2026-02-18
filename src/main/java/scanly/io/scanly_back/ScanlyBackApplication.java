package scanly.io.scanly_back;

import io.github.resilience4j.springboot3.ratelimiter.autoconfigure.RateLimiterAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude = {RateLimiterAutoConfiguration.class})	// 또는 @RateLimiter 커스텀 어노테이션명 변경(Bean 충돌)
public class ScanlyBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScanlyBackApplication.class, args);
	}

}
