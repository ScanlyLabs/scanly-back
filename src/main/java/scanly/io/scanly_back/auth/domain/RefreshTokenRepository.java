package scanly.io.scanly_back.auth.domain;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);
}
