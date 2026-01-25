package scanly.io.scanly_back.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scanly.io.scanly_back.notification.domain.PushTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushTokenService {

    private final PushTokenRepository pushTokenRepository;

    
}
