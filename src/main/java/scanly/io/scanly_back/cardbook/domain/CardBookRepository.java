package scanly.io.scanly_back.cardbook.domain;

import java.util.List;
import java.util.Optional;

public interface CardBookRepository {

    CardBook save(CardBook cardBook);

    boolean existsByMemberIdAndCardId(String memberId, String cardId);

    List<CardBook> findAllByMemberId(String memberId);

    Optional<CardBook> findByIdAndMemberId(String id, String memberId);

    CardBook update(CardBook cardBook);
}
