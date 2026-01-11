package scanly.io.scanly_back.member.domain;

public interface MemberRepository {
    Member save(Member member);
    boolean existsByLoginId(String loginId);
}
