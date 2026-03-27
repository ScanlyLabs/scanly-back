package scanly.io.scanly_back.cardbook.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import scanly.io.scanly_back.IntegrationJpaTestSupport;
import scanly.io.scanly_back.cardbook.infrastructure.CardExchangeJpaRepository;
import scanly.io.scanly_back.cardbook.infrastructure.CardExchangeMapper;
import scanly.io.scanly_back.cardbook.infrastructure.CardExchangeRepositoryImpl;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import({CardExchangeRepositoryImpl.class, CardExchangeMapper.class})
@DisplayName("CardExchangeRepository 테스트")
class CardExchangeRepositoryTest extends IntegrationJpaTestSupport {

    @Autowired
    private CardExchangeRepository cardExchangeRepository;

    @Autowired
    private CardExchangeJpaRepository cardExchangeJpaRepository;

    @AfterEach
    void after() {
        cardExchangeJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[Happy] 명함 교환 내역을 저장한다.")
    void save() {
        // given
        CardExchange cardExchange = createCardExchange();

        // when
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);

        // then
        assertThat(savedCardExchange)
                .extracting("senderId", "receiverId", "status")
                .contains(cardExchange.getSenderId(), cardExchange.getReceiverId(), cardExchange.getStatus());
    }

    @Test
    @DisplayName("[Happy] 명함 교환 아이디 및 수신자 아이디로 명함 교환 내역을 조회한다.")
    void findByIdAndReceiverIdIsSuccess() {
        // given
        CardExchange cardExchange = createCardExchange();
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
        String id = savedCardExchange.getId();
        String receiverId = savedCardExchange.getReceiverId();

        // when
        Optional<CardExchange> foundCardExchange = cardExchangeRepository.findByIdAndReceiverId(id, receiverId);

        // then
        assertThat(foundCardExchange).isPresent();
        assertThat(foundCardExchange.get())
                .extracting("id", "senderId", "receiverId", "status")
                .contains(savedCardExchange.getId(), savedCardExchange.getSenderId(), savedCardExchange.getReceiverId(), savedCardExchange.getStatus());
    }

    @Test
    @DisplayName("[Bad] 명함 교환 아이디 및 수신자 아이디로 명함 교환 내역을 조회 시 명함 교환 아이디가 다르면 조회되지 않는다.")
    void findByIdAndReceiverIdWhenIdIsDiffer() {
        // given
        CardExchange cardExchange = createCardExchange();
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
        String receiverId = savedCardExchange.getReceiverId();

        // when
        Optional<CardExchange> foundCardExchange
                = cardExchangeRepository.findByIdAndReceiverId(UUID.randomUUID().toString(), receiverId);

        // then
        assertThat(foundCardExchange).isNotPresent();
    }

    @Test
    @DisplayName("[Bad] 명함 교환 아이디 및 수신자 아이디로 명함 교환 내역을 조회 시 수신자 아이디가 다르면 조회되지 않는다.")
    void findByIdAndReceiverIdWhenReceiverIdIsDiffer() {
        // given
        CardExchange cardExchange = createCardExchange();
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
        String id = savedCardExchange.getId();

        // when
        Optional<CardExchange> foundCardExchange
                = cardExchangeRepository.findByIdAndReceiverId(id, UUID.randomUUID().toString());

        // then
        assertThat(foundCardExchange).isNotPresent();
    }

    @Test
    @DisplayName("[Happy] 명함 교환 내역 정보를 수정한다.")
    void updateStatus() {
        // given
        CardExchange cardExchange = createCardExchange();
        CardExchange savedCardExchange = cardExchangeRepository.save(cardExchange);
        savedCardExchange.accept();

        // when
        CardExchange updatedCardExchange = cardExchangeRepository.updateStatus(savedCardExchange);

        // then
        assertThat(updatedCardExchange)
                .extracting("id", "senderId", "receiverId", "status")
                .contains(savedCardExchange.getId(), savedCardExchange.getSenderId(), savedCardExchange.getReceiverId(), savedCardExchange.getStatus());
    }

    private CardExchange createCardExchange() {
        return CardExchange.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
    }
}