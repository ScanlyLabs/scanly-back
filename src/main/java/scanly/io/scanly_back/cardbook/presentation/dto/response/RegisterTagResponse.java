package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.TagInfo;

public record RegisterTagResponse(
        String id,
        String cardBookId,
        String name
) {
    public static RegisterTagResponse from(TagInfo tagInfo) {
        return new RegisterTagResponse(
                tagInfo.id(),
                tagInfo.cardBookId(),
                tagInfo.name()
        );
    }
}
