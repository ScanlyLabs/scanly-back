package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.CardExchange;
import scanly.io.scanly_back.cardbook.domain.CardExchangeRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardExchangeEntity;

@Repository
@RequiredArgsConstructor
public class CardExchangeRepositoryImpl implements CardExchangeRepository {

    private final CardExchangeJpaRepository cardExchangeJpaRepository;
    private final CardExchangeMapper cardExchangeMapper;

    /**
     * 명함 교환 내역 저장
     * @param cardExchange 명함 교환 정보
     * @return 저장된 명함교환 정보
     */
    @Override
    public CardExchange save(CardExchange cardExchange) {
        CardExchangeEntity cardExchangeEntity = cardExchangeMapper.toEntity(cardExchange);
        CardExchangeEntity savedCardExchangeEntity = cardExchangeJpaRepository.save(cardExchangeEntity);
        return cardExchangeMapper.toDomain(savedCardExchangeEntity);
    }
}
