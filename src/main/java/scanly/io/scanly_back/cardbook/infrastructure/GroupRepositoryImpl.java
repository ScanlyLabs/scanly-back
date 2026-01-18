package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.GroupEntity;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;
    private final GroupMapper groupMapper;

    /**
     * 회원 아이디로 명함첩 그룹 수 조회
     * @param memberId 회원 아이디
     * @return 그룹 수
     */
    @Override
    public long countByMemberId(String memberId) {
        return groupJpaRepository.countByMemberId(memberId);
    }

    /**
     * 명함첩 그룹 저장
     * @param group 그룹 정보
     * @return 신규 명함첩 그룹
     */
    @Override
    public Group save(Group group) {
        GroupEntity groupEntity = groupMapper.toEntity(group);
        GroupEntity savedGroupEntity = groupJpaRepository.save(groupEntity);
        return groupMapper.toDomain(savedGroupEntity);
    }
}
