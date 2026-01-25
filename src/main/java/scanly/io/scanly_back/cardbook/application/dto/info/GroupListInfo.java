package scanly.io.scanly_back.cardbook.application.dto.info;

import java.util.List;

public record GroupListInfo(
        List<DefaultGroupInfo> defaultGroups,
        List<GroupWithCountInfo> customGroups
) {
    public static GroupListInfo of(List<DefaultGroupInfo> defaultGroups, List<GroupWithCountInfo> customGroups) {
        return new GroupListInfo(defaultGroups, customGroups);
    }
}
