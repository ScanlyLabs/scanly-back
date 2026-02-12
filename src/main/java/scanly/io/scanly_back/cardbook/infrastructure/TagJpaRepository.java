package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.TagEntity;

public interface TagJpaRepository extends JpaRepository<TagEntity, String> {
    int countByCardBookId(String cardBookId);

    void deleteAllByCardBookId(String cardBookId);
}
