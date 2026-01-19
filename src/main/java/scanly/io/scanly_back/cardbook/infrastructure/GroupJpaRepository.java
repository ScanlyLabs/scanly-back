package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.GroupEntity;

import java.util.Optional;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, String> {
    long countByMemberId(String memberId);
    Optional<GroupEntity> findByIdAndMemberId(String id, String memberId);
}
