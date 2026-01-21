package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;

@Component
public class CardBookMapper {

    /**
     * CardBook domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public CardBookEntity toEntity(CardBook domain) {
        if (domain == null) {
            return null;
        }

        return CardBookEntity.of(
                domain.getId(),
                domain.getMemberId(),
                domain.getCardId(),
                domain.getProfileSnapshot(),
                domain.getGroupId(),
                domain.getMemo(),
                domain.isFavorite()
        );
    }

    /**
     * CardBook entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public CardBook toDomain(CardBookEntity entity) {
        if (entity == null) {
            return null;
        }

        return CardBook.of(
                entity.getId(),
                entity.getMemberId(),
                entity.getCardId(),
                entity.getProfileSnapshot(),
                entity.getGroupId(),
                entity.getMemo(),
                entity.isFavorite()
        );
    }
}
