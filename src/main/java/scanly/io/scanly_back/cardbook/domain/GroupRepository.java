package scanly.io.scanly_back.cardbook.domain;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    Optional<Group> findById(String id);

    Optional<Group> findByIdAndMemberId(String id, String memberId);

    List<Group> findAllByIds(List<String> ids);

    long countByMemberId(String memberId);

    Group save(Group group);

    List<Group> saveAll(List<Group> groups);

    Group rename(Group group);

    void deleteById(String id);
}
