package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.DefaultGroupInfo;

public record DefaultGroupResponse(
        String id,
        String name,
        long cardBookCount
) {

    public static DefaultGroupResponse from(DefaultGroupInfo info) {
        return new DefaultGroupResponse(
                info.id(),
                info.name(),
                info.cardBookCount()
        );
    }
}
