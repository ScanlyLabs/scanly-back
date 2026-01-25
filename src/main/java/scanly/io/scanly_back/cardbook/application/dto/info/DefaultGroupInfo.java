package scanly.io.scanly_back.cardbook.application.dto.info;

public record DefaultGroupInfo(
        String id,
        String name,
        long cardBookCount
) {
    public static DefaultGroupInfo of(String id, String name, long cardBookCount) {
        return new DefaultGroupInfo(id, name, cardBookCount);
    }
}
