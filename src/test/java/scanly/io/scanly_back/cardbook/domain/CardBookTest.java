package scanly.io.scanly_back.cardbook.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scanly.io.scanly_back.card.domain.Card;
import scanly.io.scanly_back.cardbook.domain.model.ProfileSnapshot;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CardBook 도메인 테스트")
class CardBookTest {

    @Test
    @DisplayName("명함첩 그룹을 수정한다")
    void updateGroup() {
        // given
        CardBook cardBook = defaultCardBook();
        String newGroupId = UUID.randomUUID().toString();

        // when
        cardBook.updateGroup(newGroupId);

        // then
        assertThat(cardBook.getGroupId()).isEqualTo(newGroupId);
    }

    @Test
    @DisplayName("명함첩 메모를 수정한다")
    void updateMemo() {
        // given
        CardBook cardBook = defaultCardBook();
        String memo = "새로운 메모";

        // when
        cardBook.updateMemo(memo);

        // then
        assertThat(cardBook.getMemo()).isEqualTo(memo);
    }

    @Test
    @DisplayName("명함첩 즐겨찾기를 수정한다")
    void updateFavorite() {
        // given
        CardBook cardBook = defaultCardBook();

        // when
        cardBook.updateFavorite(true);

        // then
        assertThat(cardBook.isFavorite()).isEqualTo(true);
    }

    private static CardBook defaultCardBook() {
        return CardBook.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                ProfileSnapshot.from(Card.create(
                        UUID.randomUUID().toString(), "홍길동", "개발자", "스캔리",
                        "01012341234", "test@naver.com", null, null, null, null
                )),
                UUID.randomUUID().toString()
        );
    }
}