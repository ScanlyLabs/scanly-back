package scanly.io.scanly_back.cardbook.domain;

import java.util.Optional;

public interface GroupRepository {
    Optional<Group> findById(String id);

    long countByMemberId(String memberId);

    Group save(Group group);

    void deleteById(String id);
}
