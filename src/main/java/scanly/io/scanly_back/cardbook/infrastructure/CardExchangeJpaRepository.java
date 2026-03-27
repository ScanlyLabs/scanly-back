package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardExchangeEntity;

import java.util.Optional;

public interface CardExchangeJpaRepository extends JpaRepository<CardExchangeEntity, String> {
    Optional<CardExchangeEntity> findByIdAndReceiverId(String id, String receiverId);
}
