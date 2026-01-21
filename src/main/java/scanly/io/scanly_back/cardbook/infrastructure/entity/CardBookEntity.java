package scanly.io.scanly_back.cardbook.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "card_book")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardBookEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, updatable = false)
    private String memberId;             // 소유자 ID

    @Column(name = "card_id", nullable = false, updatable = false)
    private String cardId;              // 원본 명함 ID

    @Column(name = "profile_snapshot", nullable = false, updatable = false)
    private String profileSnapshot;      // 저장 당시 명함 정보

    @Column(name = "group_id")
    private String groupId;             // 저장된 그룹 ID

    @Column(name = "memo")
    private String memo;                // 메모

    @Column(name = "is_favorite", nullable = false)
    private boolean isFavorite;         // 즐겨찾기 여부

    public static CardBookEntity of(
            String id, String memberId, String cardId,
            String profileSnapshot, String groupId,
            String memo, boolean isFavorite
    ) {
        return new CardBookEntity(
                id, memberId, cardId,
                profileSnapshot, groupId,
                memo, isFavorite
        );
    }
}
