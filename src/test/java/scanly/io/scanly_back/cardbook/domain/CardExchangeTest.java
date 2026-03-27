package scanly.io.scanly_back.cardbook.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scanly.io.scanly_back.cardbook.domain.model.ExchangeStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardExchange 도메인 테스트")
class CardExchangeTest {

    @Test
    @DisplayName("[Happy] 명함 교환 상태를 수락 상태로 변경한다.")
    void accept() {
        // given
        CardExchange cardExchange = createCardExchange();

        // when
        cardExchange.accept();

        // then
        assertThat(cardExchange.getStatus()).isEqualTo(ExchangeStatus.ACCEPTED);
    }

    @Test
    @DisplayName("[Happy] 명함 교환 상태를 거절 상태로 변경한다.")
    void reject() {
        // given
        CardExchange cardExchange = createCardExchange();

        // when
        cardExchange.reject();

        // then
        assertThat(cardExchange.getStatus()).isEqualTo(ExchangeStatus.REJECTED);
    }

    @Test
    @DisplayName("[Happy] 명함 교환 상태가 대기 상태라면 true를 리턴한다.")
    void isPending() {
        // given
        CardExchange cardExchange = createCardExchange();

        // when & then
        assertThat(cardExchange.getStatus()).isEqualTo(ExchangeStatus.PENDING);
    }

    private CardExchange createCardExchange() {
        return CardExchange.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
    }
}