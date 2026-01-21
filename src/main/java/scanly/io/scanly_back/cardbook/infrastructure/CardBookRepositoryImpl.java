package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;

@Repository
@RequiredArgsConstructor
public class CardBookRepositoryImpl implements CardBookRepository {

    private final CardBookJpaRepository cardBookJpaRepository;

}
