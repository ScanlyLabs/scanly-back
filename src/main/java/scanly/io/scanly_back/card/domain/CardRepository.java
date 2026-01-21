package scanly.io.scanly_back.card.domain;

import java.util.Optional;

public interface CardRepository {

    Card save(Card card);

    Card update(Card card);

    Card updateOnlyCard(Card card);

    boolean existsByMemberId(String memberId);

    Optional<Card> findById(String id);

    Optional<Card> findByMemberId(String memberId);

    void delete(Card card);
}
