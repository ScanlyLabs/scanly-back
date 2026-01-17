package scanly.io.scanly_back.auth.application.dto.info;

public record TokenInfo(
        String accessToken,
        String refreshToken
) {

    public static TokenInfo from(String accessToken, String refreshToken) {
        return new TokenInfo(accessToken, refreshToken);
    }
}
