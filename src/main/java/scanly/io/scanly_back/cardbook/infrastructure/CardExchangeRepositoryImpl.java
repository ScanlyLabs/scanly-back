package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.CardExchange;
import scanly.io.scanly_back.cardbook.domain.CardExchangeRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardExchangeEntity;

import java.util.Optional;

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

    /**
     * 명함 교환 내역 조회
     * @param id 명함 교환 아이디
     * @param receiverId 수신자 아이디
     * @return 조회된 명함 교환 정보
     */
    @Override
    public Optional<CardExchange> findByIdAndReceiverId(String id, String receiverId) {
        return cardExchangeJpaRepository.findByIdAndReceiverId(id, receiverId)
                .map(cardExchangeMapper::toDomain);
    }

    /**
     * 명함 교환 상태 업데이트
     * @param cardExchange 명함 교환 정보
     * @return 업데이트된 명함 교환 정보
     */
    @Override
    public CardExchange updateStatus(CardExchange cardExchange) {
        CardExchangeEntity cardExchangeEntity = cardExchangeMapper.toEntity(cardExchange);
        CardExchangeEntity savedCardExchangeEntity = cardExchangeJpaRepository.save(cardExchangeEntity);
        return cardExchangeMapper.toDomain(savedCardExchangeEntity);
    }
}
