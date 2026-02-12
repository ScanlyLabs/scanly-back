package scanly.io.scanly_back.cardbook.infrastructure;

import scanly.io.scanly_back.cardbook.domain.Tag;

public interface TagRepository {
    Tag save(Tag tag);
    int countByCardBookId(String cardBookId);
}
