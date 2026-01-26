package scanly.io.scanly_back.notification.domain;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.notification.infrastructure.entity.PushTokenEntity;

@Component
public class PushTokenMapper {

    /**
     * 푸시토큰 domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public PushTokenEntity toEntity(PushToken domain) {
        if (domain == null) {
            return null;
        }

        return PushTokenEntity.of(
                domain.getId(),
                domain.getMemberId(),
                domain.getToken(),
                domain.getPlatform()
        );
    }

    /**
     * 푸시토큰 entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public PushToken toDomain(PushTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return PushToken.of(
                entity.getId(),
                entity.getMemberId(),
                entity.getToken(),
                entity.getPlatform(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
