package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.TagEntity;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<TagEntity, String> {
    int countByCardBookId(String cardBookId);

    void deleteAllByCardBookId(String cardBookId);

    void deleteAllByCardBookIdIn(List<String> cardBookIds);

    List<TagEntity> findAllByCardBookId(String cardBookId);
}
