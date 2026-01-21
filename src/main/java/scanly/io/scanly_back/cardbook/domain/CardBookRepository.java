package scanly.io.scanly_back.cardbook.domain;

public interface CardBookRepository {

    CardBook save(CardBook cardBook);

    boolean existsByMemberIdAndCardId(String memberId, String cardId);
}
