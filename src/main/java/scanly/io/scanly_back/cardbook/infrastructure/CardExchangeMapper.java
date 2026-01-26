package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.cardbook.domain.CardExchange;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardExchangeEntity;

@Component
public class CardExchangeMapper {

    /**
     * CardExchange domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public CardExchangeEntity toEntity(CardExchange domain) {
        if (domain == null) {
            return null;
        }

        return CardExchangeEntity.of(
                domain.getId(),
                domain.getSenderId(),
                domain.getReceiverId(),
                domain.getExchangedAt()
        );
    }

    /**
     * CardExchange entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public CardExchange toDomain(CardExchangeEntity entity) {
        if (entity == null) {
            return null;
        }

        return CardExchange.of(
                entity.getId(),
                entity.getSenderId(),
                entity.getReceiverId(),
                entity.getExchangedAt()
        );
    }
}
