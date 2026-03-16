package scanly.io.scanly_back;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import scanly.io.scanly_back.common.config.JpaConfig;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig.class)
public abstract class IntegrationJpaTestSupport {
}
