package scanly.io.scanly_back.cardbook.domain;

public interface GroupRepository {
    long countByMemberId(String memberId);

    Group save(Group group);

    void deleteById(String id);
}
