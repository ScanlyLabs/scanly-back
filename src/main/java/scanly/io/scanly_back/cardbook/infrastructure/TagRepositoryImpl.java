package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.infrastructure.entity.TagEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;
    private final TagMapper tagMapper;

    /**
     * 아이디로 태그 조회
     * @param id 아이디
     * @return 조회된 태그
     */
    @Override
    public Optional<Tag> findById(String id) {
        return tagJpaRepository.findById(id).map(tagMapper::toDomain);
    }

    /**
     * 태그 저장
     * @param tag 저장할 태그 정보
     * @return 신규 태그
     */
    @Override
    public Tag save(Tag tag) {
        TagEntity tagEntity = tagMapper.toEntity(tag);
        TagEntity savedTagEntity = tagJpaRepository.save(tagEntity);
        return tagMapper.toDomain(savedTagEntity);
    }

    /**
     * 태그 개수 조회
     * @param cardBookId 명함첩 아이디
     * @return 조회된 수
     */
    @Override
    public int countByCardBookId(String cardBookId) {
        return tagJpaRepository.countByCardBookId(cardBookId);
    }

    /**
     * 태그 수정
     * @param tag 수정할 태그
     * @return 수정된 태그
     */
    @Override
    public Tag update(Tag tag) {
        TagEntity tagEntity = tagMapper.toEntity(tag);
        TagEntity updatedTagEntity = tagJpaRepository.save(tagEntity);
        return tagMapper.toDomain(updatedTagEntity);
    }

    /**
     * 아이디로 태그 삭제
     * @param id 아이디
     */
    @Override
    public void deleteById(String id) {
        tagJpaRepository.deleteById(id);
    }
}
