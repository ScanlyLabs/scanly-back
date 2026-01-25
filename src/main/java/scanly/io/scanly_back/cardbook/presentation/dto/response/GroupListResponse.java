package scanly.io.scanly_back.cardbook.presentation.dto.response;

import scanly.io.scanly_back.cardbook.application.dto.info.GroupListInfo;

import java.util.List;

public record GroupListResponse(
        List<DefaultGroupResponse> defaultGroups,
        List<GroupWithCountResponse> customGroups
) {

    public static GroupListResponse from(GroupListInfo info) {
        List<DefaultGroupResponse> defaultGroups = info.defaultGroups().stream()
                .map(DefaultGroupResponse::from)
                .toList();

        List<GroupWithCountResponse> customGroups = info.customGroups().stream()
                .map(GroupWithCountResponse::from)
                .toList();

        return new GroupListResponse(defaultGroups, customGroups);
    }
}
