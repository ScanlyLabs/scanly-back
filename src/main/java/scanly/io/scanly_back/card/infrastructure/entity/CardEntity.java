package scanly.io.scanly_back.card.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "card")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String memberId;                // 소유자 ID

    @Column(nullable = false, length = 30)
    private String name;                    // 이름

    @Column(nullable = false, length = 50)
    private String title;                   // 직함

    @Column(nullable = false, length = 50)
    private String company;                 // 회사

    @Column(nullable = false, length = 11)
    private String phone;                   // 전화번호

    @Column(nullable = false, length = 50)
    private String email;                   // 이메일

    @Column(length = 300)
    private String bio;                     // 자기소개

    private String profileImageUrl;          // 프로필 사진 url

    private String portfolioUrl;            // 포트폴리오 url

    @Column(length = 100)
    private String location;                // 회사 위치

    private String qrImageUrl;              // qr 이미지 url

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;        // 생성 일시

    @Column(nullable = false)
    private LocalDateTime updatedAt;        // 수정 일시

    public static CardEntity of(
            String id, String memberId, String name, String title, String company,
            String phone, String email, String bio, String profileImageUrl,
            String portfolioUrl, String location, String qrImageUrl,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new CardEntity(
                id, memberId, name, title, company,
                phone, email, bio, profileImageUrl,
                portfolioUrl, location, qrImageUrl,
                createdAt, updatedAt
        );
    }
}
