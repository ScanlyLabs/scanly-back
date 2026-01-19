package scanly.io.scanly_back.cardbook.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.cardbook.domain.Group;
import scanly.io.scanly_back.cardbook.domain.GroupRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.GroupEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private final GroupJpaRepository groupJpaRepository;
    private final GroupMapper groupMapper;

    /**
     * 명함첩 그룹 조회
     * @param id 명함첩 그룹 아이디
     * @return 조회된 명함첩 그룹
     */
    @Override
    public Optional<Group> findById(String id) {
        return groupJpaRepository.findById(id)
                .map(groupMapper::toDomain);
    }

    /**
     * 명함첩 그룹 단건 조회
     * @param id 명함첩 그룹 아이디
     * @param memberId 회원 아이디
     * @return 조회된 명함첩 그룹
     */
    @Override
    public Optional<Group> findByIdAndMemberId(String id, String memberId) {
        return groupJpaRepository.findByIdAndMemberId(id, memberId)
                .map(groupMapper::toDomain);
    }

    /**
     * 여러 명함첩 그룹 조회
     * @param ids 명함첩 그룹 아이디 목록
     * @return 조회된 명함첩 그룹 목록
     */
    @Override
    public List<Group> findAllByIds(List<String> ids) {
        return groupJpaRepository.findAllById(ids).stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    /**
     * 내 명함첩 그룹 목록 조회
     * @param memberId 회원 아이디
     * @return 조회된 그룹 목록
     */
    @Override
    public List<Group> findAllByMemberId(String memberId) {
        return groupJpaRepository.findAllByMemberId(memberId).stream()
                .map(groupMapper::toDomain)
                .toList();
    }

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

    /**
     * 여러 명함첩 그룹 순서 저장
     * @param groups 그룹 목록
     * @return 저장된 그룹 목록
     */
    @Override
    public List<Group> reorder(List<Group> groups) {
        List<GroupEntity> entities = groups.stream()
                .map(groupMapper::toEntity)
                .toList();
        List<GroupEntity> savedEntities = groupJpaRepository.saveAll(entities);
        return savedEntities.stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    /**
     * 명함첩 그룹명 변경
     * @param group 변경할 그룹 정보
     * @return 변경된 그룹 정보
     */
    @Override
    public Group rename(Group group) {
        GroupEntity groupEntity = groupMapper.toEntity(group);
        GroupEntity renamedGroupEntity = groupJpaRepository.save(groupEntity);
        return groupMapper.toDomain(renamedGroupEntity);
    }

    /**
     * 그룹 아이디에 해당되는 그룹 삭제
     * @param id 그룹 아이디
     */
    @Override
    public void deleteById(String id) {
        groupJpaRepository.deleteById(id);
    }
}
