package scanly.io.scanly_back.notification.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.notification.domain.PushTokenRepository;

@Repository
@RequiredArgsConstructor
public class PushTokenRepositoryImpl implements PushTokenRepository {

    private final PushTokenJpaRepository pushTokenJpaRepository;


}
