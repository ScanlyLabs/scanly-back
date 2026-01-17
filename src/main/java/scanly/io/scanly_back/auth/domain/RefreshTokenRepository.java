package scanly.io.scanly_back.auth.domain;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    Optional<String> findByMemberId(String memberId);

    void deleteByMemberId(String memberId);
}
