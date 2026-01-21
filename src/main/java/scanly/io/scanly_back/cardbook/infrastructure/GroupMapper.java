package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.infrastructure.entity.GroupEntity;

@Component
public class GroupMapper {

    /**
     * 그룹 domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public GroupEntity toEntity(Group domain) {
        if (domain == null) {
            return null;
        }

        return GroupEntity.of(
                domain.getId(),
                domain.getMemberId(),
                domain.getName(),
                domain.getSortOrder()
        );
    }

    /**
     * 그룹  entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Group toDomain(GroupEntity entity) {
        if (entity == null) {
            return null;
        }

        return Group.of(
                entity.getId(),
                entity.getMemberId(),
                entity.getName(),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
