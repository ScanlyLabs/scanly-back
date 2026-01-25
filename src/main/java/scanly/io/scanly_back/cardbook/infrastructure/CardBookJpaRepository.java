package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;

import java.util.List;

public interface CardBookJpaRepository extends JpaRepository<CardBookEntity, String> {

    boolean existsByMemberIdAndCardId(String memberId, String cardId);

    List<CardBookEntity> findAllByMemberId(String memberId);
}
