package scanly.io.scanly_back.auth.application.dto.info;

public record LoginInfo(
        String accessToken,
        String refreshToken
) {

    public static LoginInfo from(String accessToken, String refreshToken) {
        return new LoginInfo(accessToken, refreshToken);
    }
}
