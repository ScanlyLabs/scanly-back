package scanly.io.scanly_back.card.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.card.domain.CardRepository;
import scanly.io.scanly_back.card.infrastructure.entity.CardEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {

    private final CardJpaRepository cardJpaRepository;
    private final CardMapper cardMapper;

    /**
     * 명함 저장 (소셜 링크 포함 - JSONB)
     * @param card 명함 정보
     * @return 신규 명함
     */
    @Override
    public Card save(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);
        return cardMapper.toDomain(savedCardEntity);
    }

    /**
     * 명함 수정 (소셜 링크 포함 - JSONB)
     * @param card 명함 정보
     * @return 수정된 명함
     */
    @Override
    public Card update(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);
        return cardMapper.toDomain(savedCardEntity);
    }

    /**
     * 명함 수정 (소셜 링크 포함 - JSONB)
     * @param card 명함 정보
     * @return 수정된 명함
     */
    @Override
    public Card updateOnlyCard(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        CardEntity savedCardEntity = cardJpaRepository.save(cardEntity);
        return cardMapper.toDomain(savedCardEntity);
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
                .map(cardMapper::toDomain);
    }

    /**
     * 회원 아이디로 명함 조회
     * @param memberId 회원 아이디
     * @return 조회된 명함
     */
    @Override
    public Optional<Card> findByMemberId(String memberId) {
        return cardJpaRepository.findByMemberId(memberId)
                .map(cardMapper::toDomain);
    }

    /**
     * 명함 ID 목록으로 명함 조회
     * @param ids 명함 ID 목록
     * @return 조회된 명함 목록
     */
    @Override
    public List<Card> findAllByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return cardJpaRepository.findAllByIdIn(ids).stream()
                .map(cardMapper::toDomain)
                .toList();
    }

    /**
     * 회원 아이디로 내 명함 제거
     */
    @Override
    public void delete(Card card) {
        CardEntity cardEntity = cardMapper.toEntity(card);
        cardJpaRepository.deleteById(cardEntity.getId());
    }
}
