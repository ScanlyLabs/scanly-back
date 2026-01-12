package scanly.io.scanly_back.card.domain;

import java.util.Optional;

public interface CardRepository {

    Card save(Card card);

    boolean existsByMemberId(String memberId);

    Optional<Card> findByMemberId(String memberId);
}
