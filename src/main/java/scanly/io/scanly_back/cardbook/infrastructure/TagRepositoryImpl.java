package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.Tag;
import scanly.io.scanly_back.cardbook.infrastructure.entity.TagEntity;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;
    private final TagMapper tagMapper;

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
}
