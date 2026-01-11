package scanly.io.scanly_back.member.domain;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    boolean existsByLoginId(String loginId);

    Optional<Member> findById(String id);

    Optional<Member> findByLoginId(String loginId);
}
