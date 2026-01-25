package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.CardExchangeRepository;

@Repository
@RequiredArgsConstructor
public class CardExchangeRepositoryImpl implements CardExchangeRepository {

    private final CardExchangeJpaRepository cardExchangeJpaRepository;


}
