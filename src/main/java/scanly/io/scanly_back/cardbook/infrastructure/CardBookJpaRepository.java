package scanly.io.scanly_back.cardbook.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.entity.CardBookEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CardBookJpaRepository extends JpaRepository<CardBookEntity, String> {

    boolean existsByMemberIdAndCardId(String memberId, String cardId);

    List<CardBookEntity> findAllByMemberId(String memberId);

    Page<CardBookEntity> findAllByMemberId(String memberId, Pageable pageable);

    Page<CardBookEntity> findAllByMemberIdAndGroupId(String memberId, String groupId, Pageable pageable);

    Optional<CardBookEntity> findByIdAndMemberId(String id, String memberId);

    long countByMemberId(String memberId);

    long countByMemberIdAndIsFavoriteTrue(String memberId);

    long countByMemberIdAndCreatedAtAfter(String memberId, LocalDateTime after);

    long countByMemberIdAndGroupId(String memberId, String groupId);
}
