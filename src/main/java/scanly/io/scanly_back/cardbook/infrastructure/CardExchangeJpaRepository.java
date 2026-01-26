package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardExchangeEntity;

public interface CardExchangeJpaRepository extends JpaRepository<CardExchangeEntity, String> {
}
