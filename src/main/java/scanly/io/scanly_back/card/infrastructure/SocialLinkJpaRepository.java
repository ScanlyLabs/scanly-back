package scanly.io.scanly_back.card.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.card.infrastructure.entity.SocialLinkEntity;

import java.util.List;

public interface SocialLinkJpaRepository extends JpaRepository<SocialLinkEntity, String> {

    List<SocialLinkEntity> findByCardIdOrderByDisplayOrderAsc(String cardId);

    void deleteAllByCardId(String cardId);
}
