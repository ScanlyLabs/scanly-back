package scanly.io.scanly_back.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.notification.infrastructure.entity.PushTokenEntity;

import java.util.Optional;

public interface PushTokenJpaRepository extends JpaRepository<PushTokenEntity, String> {
    Optional<PushTokenEntity> findByMemberId(String memberId);
}
