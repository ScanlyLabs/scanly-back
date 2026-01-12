package scanly.io.scanly_back.card.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scanly.io.scanly_back.card.domain.SocialLinkType;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "social_link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
    private CardEntity card;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SocialLinkType type;                    // 소셜 링크 타입

    @Column(nullable = false, length = 500)
    private String url;                             // url

    @Column(nullable = false)
    private int displayOrder;                       // 표시 순서

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;                // 생성 일시

    @Column(nullable = false)
    private LocalDateTime updatedAt;                // 수정 일시

    private SocialLinkEntity(
            String id, SocialLinkType type, String url,
            int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SocialLinkEntity of(
            String id, SocialLinkType type, String url,
            int displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new SocialLinkEntity(
                id, type, url,
                displayOrder, createdAt, updatedAt
        );
    }
}
