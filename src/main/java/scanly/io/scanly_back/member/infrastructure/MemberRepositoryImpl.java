package scanly.io.scanly_back.member.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.domain.MemberRepository;
import scanly.io.scanly_back.member.infrastructure.entity.MemberEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    /**
     * 회원 가입
     * @param member 회원 정보
     * @return 신규 회원
     */
    @Override
    public Member save(Member member) {
        MemberEntity memberEntity = memberMapper.toEntity(member);
        MemberEntity savedMemberEntity = memberJpaRepository.save(memberEntity);

        return memberMapper.toDomain(savedMemberEntity);
    }

    /**
     * 로그인 아이디 중복 확인
     * @param loginId 로그인 아이디
     * @return 중복 여부
     */
    @Override
    public boolean existsByLoginId(String loginId) {
        return memberJpaRepository.existsByLoginId(loginId);
    }

    /**
     * 아이디로 회원 조회
     * @param id 회원 아이디
     * @return 조회된 회원
     */
    @Override
    public Optional<Member> findById(String id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toDomain);
    }

    /**
     * 로그인 아이디로 회원 조회
     * @param loginId 로그인 아이디
     * @return 조회된 회원
     */
    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberJpaRepository.findByLoginId(loginId)
                .map(memberMapper::toDomain);
    }
}
