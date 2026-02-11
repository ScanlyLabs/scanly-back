package scanly.io.scanly_back.card.domain;

import java.util.List;
import java.util.Optional;

public interface CardRepository {

    Card save(Card card);

    Card update(Card card);

    Card updateOnlyCard(Card card);

    boolean existsByMemberId(String memberId);

    Optional<Card> findById(String id);

    Optional<Card> findByMemberId(String memberId);

    List<Card> findAllByIds(List<String> ids);

    void delete(Card card);
}
