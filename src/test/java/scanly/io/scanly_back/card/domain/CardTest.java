package scanly.io.scanly_back.card.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import scanly.io.scanly_back.common.exception.CustomException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Card 도메인 테스트")
class CardTest {

    @Test
    @DisplayName("명함 정보를 수정한다")
    void updateCard() {
        // given
        Card card = defaultCard();

        // when
        card.update(
                "시니어 개발자", "스캔리랩스",
                "010-9999-9999", "senior@scanly.io", "반갑습니다",
                "profile.jpg", "portfolio.com", "부산"
        );

        // then
        assertThat(card.getTitle()).isEqualTo("시니어 개발자");
        assertThat(card.getCompany()).isEqualTo("스캔리랩스");
        assertThat(card.getPhone()).isEqualTo("010-9999-9999");
        assertThat(card.getEmail()).isEqualTo("senior@scanly.io");
        assertThat(card.getBio()).isEqualTo("반갑습니다");
        assertThat(card.getProfileImageUrl()).isEqualTo("profile.jpg");
        assertThat(card.getPortfolioUrl()).isEqualTo("portfolio.com");
        assertThat(card.getLocation()).isEqualTo("부산");
    }

    @Nested
    @DisplayName("소셜 링크 테스트")
    class SocialLink {
        @Test
        @DisplayName("소셜링크를 추가한다")
        void addSocialLink() {
            // given
            Card card = defaultCard();

            // when
            card.addSocialLink(SocialLinkType.GITHUB, "https://github.com/hong");

            // then
            assertThat(card.getSocialLinks()).hasSize(1);
            assertThat(card.getSocialLinks().getFirst().getType()).isEqualTo(SocialLinkType.GITHUB);
            assertThat(card.getSocialLinks().getFirst().getUrl()).isEqualTo("https://github.com/hong");
        }

        @Test
        @DisplayName("소셜 링크는 최대 10개까지 추가할 수 있다.")
        void socialLinkLimit() {
            Card card = defaultCard();

            for (int i = 0; i < 10; i++) {
                card.addSocialLink(SocialLinkType.GITHUB, "url"+i);
            }

            assertThatThrownBy(() -> card.addSocialLink(SocialLinkType.GITHUB, "11번째 url"))
                    .isInstanceOf(CustomException.class);
        }

        @Test
        @DisplayName("모든 소셜링크를 삭제한다")
        void clearAllSocialLinks() {
            // given
            Card card = defaultCard();
            card.addSocialLink(SocialLinkType.GITHUB, "https://github.com/hong");
            card.addSocialLink(SocialLinkType.LINKEDIN, "https://linkedin.com/in/hong");

            // when
            card.clearSocialLinks();

            // then
            assertThat(card.getSocialLinks()).isEmpty();
        }
    }

    
    @Test
    @DisplayName("QR 코드 이미지 URL을 설정한다")
    void assignQrCode() {
        // given
        Card card = defaultCard();
        String qrImageUrl = "https://s3.amazonaws.com/qr/card-1.png";

        // when
        card.assignQrCode(qrImageUrl);

        // then
        assertThat(card.getQrImageUrl()).isEqualTo(qrImageUrl);
    }

    private static Card defaultCard() {
        return Card.create(
                UUID.randomUUID().toString(), "홍길동", "개발자", "스캔리",
                "01012341234", "test@naver.com", null, null, null, null
        );
    }
}
