package scanly.io.scanly_back.card.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;
import scanly.io.scanly_back.card.infrastructure.entity.SocialLinkEntity;

import java.util.List;

@Component
public class CardMapper {

    /**
     * Card domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public CardEntity toEntity(Card domain) {
        CardEntity entity = CardEntity.of(
                domain.getId(),
                domain.getMemberId(),
                domain.getName(),
                domain.getTitle(),
                domain.getCompany(),
                domain.getPhone(),
                domain.getEmail(),
                domain.getBio(),
                domain.getProfileImageUrl(),
                domain.getPortfolioUrl(),
                domain.getLocation(),
                domain.getQrImageUrl(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );

        for (SocialLink socialLink : domain.getSocialLinks()) {
            SocialLinkEntity linkEntity = toEntity(socialLink);
            entity.addSocialLink(linkEntity);
        }

        return entity;
    }

    /**
     * SocialLink domain -> entity 객체 변환
     * @param domain 도메인
     * @return 엔티티
     */
    public SocialLinkEntity toEntity(SocialLink domain) {
        return SocialLinkEntity.of(
                domain.getId(),
                domain.getType(),
                domain.getUrl(),
                domain.getDisplayOrder(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    /**
     * Card entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Card toDomain(CardEntity entity) {
        List<SocialLink> socialLinks = entity.getSocialLinks().stream()
                .map(this::toDomain)
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
     * SocialLink entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public SocialLink toDomain(SocialLinkEntity entity) {
        return SocialLink.of(
                entity.getId(),
                entity.getType(),
                entity.getUrl(),
                entity.getDisplayOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
