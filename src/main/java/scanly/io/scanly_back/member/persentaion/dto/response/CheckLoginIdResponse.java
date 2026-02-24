package scanly.io.scanly_back.member.persentaion.dto.response;

public record CheckLoginIdResponse(
        String loginId,
        boolean available
) {

    public static CheckLoginIdResponse of(String loginId, boolean available) {
        return new CheckLoginIdResponse(loginId, available);
    }
}
