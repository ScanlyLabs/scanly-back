package scanly.io.scanly_back.cardbook.application.dto.info;

import scanly.io.scanly_back.cardbook.domain.Tag;

public record TagInfo(
        String id,
        String cardBookId,
        String name
) {
    public static TagInfo from(Tag savedTag) {
        return new TagInfo(
                savedTag.getId(),
                savedTag.getCardBookId(),
                savedTag.getName()
        );
    }
}
