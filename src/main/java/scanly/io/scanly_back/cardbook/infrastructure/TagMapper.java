package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.infrastructure.entity.TagEntity;

@Component
public class TagMapper {

    /**
     * 태그 도메인 -> 엔티티 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public TagEntity toEntity(Tag domain) {
        if (domain == null) {
            return null;
        }

        return TagEntity.of(
                domain.getId(),
                domain.getCardBookId(),
                domain.getName()
        );
    }

    /**
     * 태그 엔티티 -> 도메인 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Tag toDomain(TagEntity entity) {
        if (entity == null) {
            return null;
        }

        return Tag.of(
                entity.getId(),
                entity.getCardBookId(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
