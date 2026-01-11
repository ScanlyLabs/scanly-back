package scanly.io.scanly_back.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.member.infrastructure.entity.MemberEntity;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, String> {
    boolean existsByLoginId(String loginId);

    Optional<MemberEntity> findByLoginId(String loginId);
}
