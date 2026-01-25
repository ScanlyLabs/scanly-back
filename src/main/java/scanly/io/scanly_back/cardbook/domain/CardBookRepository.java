package scanly.io.scanly_back.cardbook.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CardBookRepository {

    CardBook save(CardBook cardBook);

    boolean existsByMemberIdAndCardId(String memberId, String cardId);

    List<CardBook> findAllByMemberId(String memberId);

    Page<CardBook> findAllByMemberId(String memberId, Pageable pageable);

    Optional<CardBook> findByIdAndMemberId(String id, String memberId);

    CardBook update(CardBook cardBook);

    void deleteById(String id);
}
