package scanly.io.scanly_back.cardbook.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card_book_group")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;                // 소유자 ID

    @Column(name = "name", nullable = false, length = 30)
    private String name;                    // 그룹명

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;                      // 정렬 순서

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;                // 생성 일시

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;                // 수정 일시

    public static GroupEntity of(
            String id, String memberId, String name, int sortOrder,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new GroupEntity(id, memberId, name, sortOrder, createdAt, updatedAt);
    }
}
