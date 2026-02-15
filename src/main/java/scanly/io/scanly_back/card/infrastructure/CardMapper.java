package scanly.io.scanly_back.card.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;
import scanly.io.scanly_back.card.infrastructure.entity.SocialLinkData;

import java.util.List;

@Component
public class CardMapper {

    /**
     * Card domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public CardEntity toEntity(Card domain) {
        if (domain == null) {
            return null;
        }

        List<SocialLinkData> socialLinkDataList = domain.getSocialLinks().stream()
                .map(this::toSocialLinkData)
                .toList();

        return CardEntity.of(
                domain.getId(),
                domain.getMemberId(),
                domain.getName(),
                domain.getTitle(),
                domain.getCompany(),
                domain.getPhone(),
                domain.getEmail(),
                domain.getBio(),
                socialLinkDataList,
                domain.getProfileImageUrl(),
                domain.getPortfolioUrl(),
                domain.getLocation(),
                domain.getQrImageUrl()
        );
    }

    /**
     * Card entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Card toDomain(CardEntity entity) {
        if (entity == null) {
            return null;
        }

        List<SocialLink> socialLinks = entity.getSocialLinks().stream()
                .map(this::toSocialLinkDomain)
                .toList();

        return Card.of(
                entity.getId(),
                entity.getMemberId(),
                entity.getName(),
                entity.getTitle(),
                entity.getCompany(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getBio(),
                socialLinks,
                entity.getProfileImageUrl(),
                entity.getPortfolioUrl(),
                entity.getLocation(),
                entity.getQrImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * SocialLink domain -> JSONB 객체 변환
     * @param domain 도메인
     * @return JSONB 저장용 데이터
     */
    private SocialLinkData toSocialLinkData(SocialLink domain) {
        return SocialLinkData.of(
                domain.getType(),
                domain.getUrl()
        );
    }

    /**
     * SocialLink JSONB -> domain 객체 변환
     * @param data JSONB 데이터
     * @return 도메인
     */
    private SocialLink toSocialLinkDomain(SocialLinkData data) {
        return SocialLink.of(
                data.type(),
                data.url()
        );
    }
}
