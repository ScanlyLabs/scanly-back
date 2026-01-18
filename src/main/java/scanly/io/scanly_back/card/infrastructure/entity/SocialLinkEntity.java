package scanly.io.scanly_back.card.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scanly.io.scanly_back.card.domain.SocialLinkType;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "social_link")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "card_id", nullable = false)
    private String cardId;                          // 명함 id

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private SocialLinkType type;                    // 소셜 링크 타입

    @Column(name = "url", nullable = false, length = 500)
    private String url;                             // url

    @Column(name = "display_order", nullable = false)
    private int displayOrder;                       // 표시 순서

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;                // 생성 일시

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;                // 수정 일시

    public static SocialLinkEntity of(
            String id, String cardId, SocialLinkType type, String url,
            int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new SocialLinkEntity(
                id, cardId, type, url,
                displayOrder, createdAt, updatedAt
        );
    }
}
