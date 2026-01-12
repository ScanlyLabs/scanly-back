package scanly.io.scanly_back.card.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.card.domain.SocialLink;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;
import scanly.io.scanly_back.card.infrastructure.entity.SocialLinkEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final CardJpaRepository cardJpaRepository;
    private final CardMapper cardMapper;

    /**
     * 명함 저장
     * @param card 명함 정보
     * @return 신규 명함
     */
    @Override
    public Card save(Card card) {
        CardEntity entity  = cardMapper.toEntity(card);
        CardEntity savedEntity = cardJpaRepository.save(entity);
        return cardMapper.toDomain(savedEntity);
    }

    /**
     * 명함 수정
     * @param card 명함 정보
     * @return 수정된 명함
     */
    @Override
    public Card update(Card card) {
        CardEntity entity = cardJpaRepository.findByMemberId(card.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("명함을 찾을 수 없습니다."));

        entity.update(
                card.getName(),
                card.getTitle(),
                card.getCompany(),
                card.getPhone(),
                card.getEmail(),
                card.getBio(),
                card.getProfileImageUrl(),
                card.getPortfolioUrl(),
                card.getLocation()
        );

        entity.clearSocialLinks();
        for (SocialLink socialLink : card.getSocialLinks()) {
            SocialLinkEntity linkEntity = cardMapper.toEntity(socialLink);
            entity.addSocialLink(linkEntity);
        }

        return cardMapper.toDomain(entity);
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
     * @return 조호된 명함
     */
    @Override
    public Optional<Card> findByMemberId(String memberId) {
        return cardJpaRepository.findByMemberId(memberId).map(cardMapper::toDomain);
    }
}
