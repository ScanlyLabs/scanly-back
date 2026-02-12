package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.TagInfo;

public record TagResponse(
        String id,
        String cardBookId,
        String name
) {
    public static TagResponse from(TagInfo tagInfo) {
        return new TagResponse(
                tagInfo.id(),
                tagInfo.cardBookId(),
                tagInfo.name()
        );
    }
}
