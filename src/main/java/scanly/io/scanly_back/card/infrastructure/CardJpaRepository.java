package scanly.io.scanly_back.card.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;

import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<CardEntity, String> {

    Optional<CardEntity> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);
}
