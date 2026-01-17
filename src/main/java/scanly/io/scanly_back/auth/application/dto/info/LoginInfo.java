package scanly.io.scanly_back.auth.application.dto.info;

public record LoginInfo(
        String id,
        String loginId,
        String accessToken,
        String refreshToken
) {

    public static LoginInfo from(String id, String loginId, String accessToken, String refreshToken) {
        return new LoginInfo(id, loginId, accessToken, refreshToken);
    }
}
