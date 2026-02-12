package scanly.io.scanly_back.cardbook.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "tag")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "card_book_id", nullable = false, updatable = false)
    private String cardBookId;             // 명함첩 ID

    @Column(name = "name", nullable = false)
    private String name;                // 태그명

    public static TagEntity of(
            String id, String memberId, String name
    ) {
        return new TagEntity(id, memberId, name);
    }
}
