package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;

public interface CardBookJpaRepository extends JpaRepository<CardBookEntity, String> {
}
