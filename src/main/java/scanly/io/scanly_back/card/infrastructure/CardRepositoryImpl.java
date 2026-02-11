package scanly.io.scanly_back.card.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
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
        // 1. 명함 저장
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);

        // 2. 소셜 링크 저장
        List<SocialLinkEntity> socialLinkEntities = card.getSocialLinks().stream()
                .map(socialLink -> cardMapper.socialLinkToEntity(socialLink, savedCardEntity.getId()))
                .toList();
        List<SocialLinkEntity> savedSocialLinkEntities = socialLinkJpaRepository.saveAll(socialLinkEntities);

        return cardMapper.toDomain(savedCardEntity, savedSocialLinkEntities);
    }

    /**
     * 명함 수정
     * 1. 명함 수정
     * 2. 소셜 링크 제거
     * 3. 소셜 링크 저장
     * @param card 명함 정보
     * @return 수정된 명함
     */
    @Override
    public Card update(Card card) {
        // 1. 명함 수정
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);

        String cardId = savedCardEntity.getId();
        
        // 2. 소셜 링크 제거
        socialLinkJpaRepository.deleteAllByCardId(cardId);

        // 3. 소셜 링크 저장
        List<SocialLinkEntity> socialLinkEntities = card.getSocialLinks().stream()
                .map(socialLink -> cardMapper.socialLinkToNewEntity(socialLink, cardId))
                .toList();
        List<SocialLinkEntity> savedSocialLinkEntities = socialLinkJpaRepository.saveAll(socialLinkEntities);

        return cardMapper.toDomain(savedCardEntity, savedSocialLinkEntities);
    }

    /**
     * 명함 수정
     * @param card 명함 정보
     * @return 수정된 명함
     */
    @Override
    public Card updateOnlyCard(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);

        List<SocialLinkEntity> socialLinkEntities = card.getSocialLinks().stream()
                .map(socialLink -> cardMapper.socialLinkToEntity(socialLink, savedCardEntity.getId()))
                .toList();

        return cardMapper.toDomain(savedCardEntity, socialLinkEntities);
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
     * 명함 ID로 명함 조회
     * @param id 명함 ID
     * @return 조회된 명함
     */
    @Override
    public Optional<Card> findById(String id) {
        return cardJpaRepository.findById(id)
                .map(cardEntity -> {
                    List<SocialLinkEntity> socialLinks = socialLinkJpaRepository
                            .findByCardIdOrderByDisplayOrderAsc(cardEntity.getId());
                    return cardMapper.toDomain(cardEntity, socialLinks);
                });
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

    /**
     * 명함 ID 목록으로 명함 조회 (SocialLink 제외)
     * @param ids 명함 ID 목록
     * @return 조회된 명함 목록
     */
    @Override
    public List<Card> findAllByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return cardJpaRepository.findAllByIdIn(ids).stream()
                .map(cardEntity -> cardMapper.toDomain(cardEntity, List.of()))
                .toList();
    }

    /**
     * 회원 아이디로 내 명함 제거
     * 1. 소셜 링크 제거
     * 2. 명함 제거
     */
    @Override
    public void delete(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);

        String cardId = cardEntity.getId();
        // 1. 소셜 링크 제거
        socialLinkJpaRepository.deleteAllByCardId(cardId);
        // 2. 명함 제거
        cardJpaRepository.deleteById(cardId);
    }
}
