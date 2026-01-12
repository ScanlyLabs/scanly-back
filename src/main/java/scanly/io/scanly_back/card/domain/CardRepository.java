package scanly.io.scanly_back.card.domain;

public interface CardRepository {

    Card save(Card card);

    boolean existsByMemberId(String memberId);
}
