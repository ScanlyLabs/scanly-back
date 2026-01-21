package scanly.io.scanly_back.member.infrastructure;

import org.springframework.stereotype.Component;
import scanly.io.scanly_back.member.domain.Member;
import scanly.io.scanly_back.member.infrastructure.entity.MemberEntity;

@Component
public class MemberMapper {

    /**
     * Member domain -> entity 객체 변환
     * @param member 도메인
     * @return 엔티티
     */
    public MemberEntity toEntity(Member member) {
        if (member == null) {
            return null;
        }

        return MemberEntity.of(
                member.getId(),
                member.getLoginId(),
                member.getPassword(),
                member.getEmail(),
                member.getStatus(),
                member.getWithdrawnAt()
        );
    }

    /**
     * Member entity -> domain 객체 변환
     * @param entity 엔티티
     * @return 도메인
     */
    public Member toDomain(MemberEntity entity) {
        if (entity == null) {
            return null;
        }

        return Member.of(
                entity.getId(),
                entity.getLoginId(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getStatus(),
                entity.getWithdrawnAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
