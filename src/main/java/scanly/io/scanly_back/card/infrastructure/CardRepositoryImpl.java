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
     * 회원 아이디로 유효성 체크
     * @param memberId 회원 아이디
     * @return 존재하면 true, 아니면 false
     */
    @Override
    public boolean existsByMemberId(String memberId) {
        return cardJpaRepository.existsByMemberId(memberId);
    }
}
