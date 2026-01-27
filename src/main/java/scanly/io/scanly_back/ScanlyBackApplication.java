package scanly.io.scanly_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ScanlyBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScanlyBackApplication.class, args);
	}

}
