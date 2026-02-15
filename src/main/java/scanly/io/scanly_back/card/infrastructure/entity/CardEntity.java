package scanly.io.scanly_back.card.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import scanly.io.scanly_back.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "card")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;                // 소유자 ID

    @Column(name = "name", nullable = false, length = 30)
    private String name;                    // 이름

    @Column(name = "title", nullable = false, length = 50)
    private String title;                   // 직함

    @Column(name = "company", nullable = false, length = 50)
    private String company;                 // 회사

    @Column(name = "phone", nullable = false, length = 11)
    private String phone;                   // 전화번호

    @Column(name = "email", nullable = false, length = 50)
    private String email;                   // 이메일

    @Column(name = "bio", length = 300)
    private String bio;                     // 자기소개

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "social_links", columnDefinition = "jsonb")
    private List<SocialLinkData> socialLinks = new ArrayList<>();  // 소셜 링크 (JSONB)

    @Column(name = "profile_image_url")
    private String profileImageUrl;          // 프로필 사진 url

    @Column(name = "portfolio_url")
    private String portfolioUrl;            // 포트폴리오 url

    @Column(name = "location", length = 100)
    private String location;                // 회사 위치

    @Column(name = "qr_image_url")
    private String qrImageUrl;              // qr 이미지 url

    public static CardEntity of(
            String id, String memberId, String name, String title, String company,
            String phone, String email, String bio, List<SocialLinkData> socialLinks,
            String profileImageUrl, String portfolioUrl, String location, String qrImageUrl
    ) {
        return new CardEntity(
                id, memberId, name, title, company,
                phone, email, bio, socialLinks != null ? socialLinks : new ArrayList<>(),
                profileImageUrl, portfolioUrl, location, qrImageUrl
        );
    }
}
