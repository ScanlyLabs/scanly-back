package scanly.io.scanly_back.cardbook.domain;

import java.util.List;

public interface CardBookRepository {

    CardBook save(CardBook cardBook);

    boolean existsByMemberIdAndCardId(String memberId, String cardId);

    List<CardBook> findAllByMemberId(String memberId);
}
