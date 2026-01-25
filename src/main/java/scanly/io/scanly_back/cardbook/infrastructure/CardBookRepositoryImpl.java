package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.CardBook;
import scanly.io.scanly_back.cardbook.domain.CardBookRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardBookRepositoryImpl implements CardBookRepository {

    private final CardBookJpaRepository cardBookJpaRepository;
    private final CardBookMapper cardBookMapper;

    /**
     * 명함첩 저장
     * @param cardBook 명함첩 정보
     * @return 저장된 명함첩
     */
    @Override
    public CardBook save(CardBook cardBook) {
        CardBookEntity entity = cardBookMapper.toEntity(cardBook);
        CardBookEntity savedEntity = cardBookJpaRepository.save(entity);
        return cardBookMapper.toDomain(savedEntity);
    }

    /**
     * 회원 아이디 및 명함 아이디로 유효성 체크
     * @param memberId 회원 아이디
     * @param cardId 명함 아이디
     * @return 존재하면 true, 아니면 false
     */
    @Override
    public boolean existsByMemberIdAndCardId(String memberId, String cardId) {
        return cardBookJpaRepository.existsByMemberIdAndCardId(memberId, cardId);
    }

    /**
     * 회원 아이디로 명함첩 목록 조회
     * @param memberId 회원 아이디
     * @return 조회된 명함첩 목록
     */
    @Override
    public List<CardBook> findAllByMemberId(String memberId) {
        List<CardBookEntity> cardBookEntities = cardBookJpaRepository.findAllByMemberId(memberId);
        return cardBookEntities.stream().map(cardBookMapper::toDomain).toList();
    }

    /**
     * 명함첩 아이디 및 회원 아이디로 명함첩 조회
     * @param id 명함첩 아이디
     * @param memberId 회원 아이디
     * @return 조회된 명함첩
     */
    @Override
    public Optional<CardBook> findByIdAndMemberId(String id, String memberId) {
        Optional<CardBookEntity> cardBookEntity = cardBookJpaRepository.findByIdAndMemberId(id, memberId);
        return cardBookEntity.map(cardBookMapper::toDomain);
    }

    /**
     * 명함첩 수정
     * @param cardBook 수정할 명함첩 정보
     * @return 수정된 명함첩
     */
    @Override
    public CardBook update(CardBook cardBook) {
        CardBookEntity cardBookEntity = cardBookMapper.toEntity(cardBook);
        CardBookEntity updatedCardBookEntity = cardBookJpaRepository.save(cardBookEntity);
        return cardBookMapper.toDomain(updatedCardBookEntity);
    }
}
