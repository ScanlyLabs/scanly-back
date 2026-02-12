package scanly.io.scanly_back.cardbook.infrastructure;

import scanly.io.scanly_back.cardbook.domain.Tag;

import java.util.Optional;

public interface TagRepository {
    Optional<Tag> findById(String id);

    Tag save(Tag tag);

    int countByCardBookId(String cardBookId);

    Tag update(Tag tag);
}
