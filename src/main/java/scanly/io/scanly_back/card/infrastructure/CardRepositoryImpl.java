package scanly.io.scanly_back.card.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;
import scanly.io.scanly_back.card.infrastructure.entity.SocialLinkEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final CardJpaRepository cardJpaRepository;
    private final SocialLinkJpaRepository socialLinkJpaRepository;
    private final CardMapper cardMapper;

    /**
     * 명함 저장
     * 1. 명함 저장
     * 2. 소셜 링크 저장
     * @param card 명함 정보
     * @return 신규 명함
     */
    @Override
    public Card save(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);

        List<SocialLinkEntity> socialLinkEntities = card.getSocialLinks().stream()
                .map(socialLink -> cardMapper.socialLinkToEntity(socialLink, savedCardEntity.getId()))
                .toList();
        List<SocialLinkEntity> savedSocialLinkEntities = socialLinkJpaRepository.saveAll(socialLinkEntities);

        return cardMapper.toDomain(savedCardEntity, savedSocialLinkEntities);
    }

    /**
     * 회원 아이디로 유효성 체크
     * @param memberId 회원 아이디
     * @return 존재하면 true, 아니면 false
     */
    @Override
    public boolean existsByMemberId(String memberId) {
        return cardJpaRepository.existsByMemberId(memberId);
    }

    /**
     * 회원 아이디로 명함 조회
     * @param memberId 회원 아이디
     * @return 조회된 명함
     */
    @Override
    public Optional<Card> findByMemberId(String memberId) {
        return cardJpaRepository.findByMemberId(memberId)
                .map(cardEntity -> {
                    List<SocialLinkEntity> socialLinks = socialLinkJpaRepository
                            .findByCardIdOrderByDisplayOrderAsc(cardEntity.getId());
                    return cardMapper.toDomain(cardEntity, socialLinks);
                });
    }
}
